package fastpayments.repository

import fastpayments.model._
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._
import fastpayments.db.AccountDB._
import fastpayments.db.CashbackDB._
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}

/**
 * The `AccountRepositoryDB` class is responsible for interacting with the database to perform
 * CRUD operations and other account-related functionality.
 *
 * @param ec the execution context for asynchronous operations
 * @param db the database instance
 */
class AccountRepositoryDB(implicit val ec: ExecutionContext, db: Database) extends AccountRepository with TransferOperations {

  /**
   * Retrieves a list of all accounts.
   *
   * @return a Future containing a sequence of accounts
   */
  override def list(): Future[Seq[Account]] = {
    db.run(accountTable.result)
  }

  /**
   * Creates a new account with the provided account details.
   *
   * @param create the account details for creation
   * @return a Future containing the created account
   */
  override def create(create: CreateAccount): Future[Account] = {
    val item = Account(username = create.username, balance = create.balance)
    for {
      _ <- db.run(accountTable += item)
      res <- get(item.id)
    } yield res
  }

  /**
   * Finds an account with the specified id.
   *
   * @param id the id of the account to find
   * @return a Future containing an optional account
   */
  def find(id: UUID): Future[Option[Account]] = {
    db.run(accountTable.filter(_.id === id).result.headOption)
  }

  /**
   * Updates an existing account with the provided details.
   *
   * @param update the updated account details
   * @return a Future containing an optional account (updated account if found, None otherwise)
   */
  override def update(update: UpdateAccount): Future[Option[Account]] = {
    val query = accountTable.filter(_.id === update.id)

    val updateQuery = (update.balance, update.username) match {
      case (Some(sum), Some(username)) => query.map(a => (a.balance, a.username)).update((sum, username))
      case (Some(sum), None) => query.map(a => a.balance).update(sum)
      case (None, Some(username)) => query.map(a => a.username).update(username)
    }

    db.run(updateQuery)
    find(update.id)
  }

  /**
   * Retrieves the account with the specified id.
   *
   * @param id the id of the account to retrieve
   * @return a Future containing the retrieved account
   */
  override def get(id: UUID): Future[Account] = {
    db.run(accountTable.filter(_.id === id).result.head)
  }

  /**
   * Deletes the account with the specified id.
   *
   * @param id the id of the account to delete
   * @return a Future representing the completion of the deletion
   */
  override def delete(id: UUID): Future[Unit] = Future {
    db.run(accountTable.filter(_.id === id).delete).map(_ => ())
  }

  /**
   * Replenishes the balance of an account with the specified amount.
   *
   * @param replenishItem the replenish item containing the account id and amount
   * @return a Future containing an `Either` result, where `Right` represents the updated account and
   *         `Left` represents an error message if the replenishment failed
   */
  override def replenish(replenishItem: ReplenishItem): Future[Either[String, Account]] = {
    for {
      balance <- db.run(accountTable.filter(_.id === replenishItem.id).map(x => x.balance).result.headOption)
      either: Either[String, Int] <- balance match {
        case Some(balance) =>
          db.run {
            accountTable.filter(_.id === replenishItem.id).map(x => x.balance).update(balance + replenishItem.amount)
          }.map(Right(_))
        case None => Future.successful(Left("Element not found"))
      }

      res <- either match {
        case Right(_) => find(replenishItem.id).map(maybeAccount => maybeAccount.map(account => Right(account)).getOrElse(Left("No such account")))
        case Left(error) => Future.successful(Left(error))
      }
    } yield res
  }

  /**
   * Withdraws an amount from the balance of an account.
   *
   * @param withdrawItem the withdraw item containing the account id and amount
   * @return a Future containing an `Either` result, where `Right` represents the updated account and
   *         `Left` represents an error message if the withdrawal failed
   */
  override def withdraw(withdrawItem: WithdrawItem): Future[Either[String, Account]] = {
    for {
      balance <- db.run(accountTable.filter(_.id === withdrawItem.id).map(x => x.balance).result.headOption)
      either: Either[String, Int] <- balance match {
        case Some(balance) if balance >= withdrawItem.amount =>
          db.run {
            accountTable.filter(_.id === withdrawItem.id).map(x => x.balance).update(balance - withdrawItem.amount)
          }.map(Right(_))
        case Some(balance) if balance < withdrawItem.amount => Future.successful(Left("Insufficient funds"))
        case None => Future.successful(Left("Element not found"))
      }

      res <- either match {
        case Right(_) => find(withdrawItem.id).map(maybeAccount => maybeAccount.map(account => Right(account)).getOrElse(Left("No such account")))
        case Left(error) => Future.successful(Left(error))
      }
    } yield res
  }

  /**
   * Retrieves the cashback percentage associated with a category.
   *
   * @param catid the id of the category
   * @return a Future containing the cashback percentage
   */
  def getCashback(catid: UUID): Future[Float] = {
    for {
      result <- db.run(cashbackTable.filter(_.id === catid).map(x => x.percent).result.headOption)
      percent = result match {
        case Some(percent) => percent
        case None => 0
      }
    } yield percent
  }

  /**
   * Transfers an amount from one account to another.
   *
   * @param transferItem the transfer item containing the source account id, destination account id,
   *                     amount, and optional category id
   * @return a Future containing an `Either` result, where `Right` represents the transfer response
   *         with the source and destination accounts, and `Left` represents an error message if the transfer failed
   */
  override def transfer(transferItem: TransferItem): Future[Either[String, TransferResponse]] = {
    for {
      withdrawRes <- withdraw(WithdrawItem(transferItem.from, transferItem.amount))
      result <- withdrawRes match {
        case Right(rightW) =>
        {
          transferItem.category_id match {
            case Some(cat) => replenish(ReplenishItem(transferItem.from, Await.result(getCashback(cat), 10.seconds) * transferItem.amount))
            case None => {}
          }
          replenish(ReplenishItem(transferItem.to, transferItem.amount)).map {
            replenishRes =>
              replenishRes.map { rightR =>
                TransferResponse(rightW, rightR)
              }
          }
        }

        case Left(error) => Future.successful(Left(error))
      }
    } yield result
  }
}