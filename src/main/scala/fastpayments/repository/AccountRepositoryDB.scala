package fastpayments.repository
import fastpayments.db.AccountDB._
import fastpayments.model._
import slick.jdbc.PostgresProfile.api._
import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}

class AccountRepositoryDB(implicit val ec: ExecutionContext, db: Database) extends AccountRepository with TransferTypes{
  /**
   * Returns a sequence of all accounts.
   *
   * @return a Future containing a sequence of accounts
   */
  override def list(): Future[Seq[Account]] = {
    db.run(accountTable.result)
  }

  /**
   * Returns the account with the given id.
   *
   * @param id the unique identifier of the account to retrieve
   * @return a Future containing the account
   */
  override def get(id: UUID): Future[Account] = {
    db.run(accountTable.filter(_.id === id).result.head)
  }
  /**
   * Returns the account with the given id, or None if no such account exists.
   *
   * @param id the unique identifier of the account to retrieve
   * @return a Future containing the account, or None if no such account exists
   */
  def find(id: UUID): Future[Option[Account]] = {
    db.run(accountTable.filter(_.id === id).result.headOption)
  }

  /**
   * Creates a new account.
   *
   * @param createAccount a JSON object containing the request to create a new account
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
   * Replenishes the balance of an existing account.
   *
   * @param replenishAccount a JSON object containing the request to replenish an account, along with the amount to
   *                         add to its balance
   * @return a Future containing the updated account, or None if no such account exists or if the amount to be
   *         replenished is less than or equal to zero
   */
  override def replenishAccount(replenishAccount: ReplenishAccount): Future[Option[Account]] = {
    val query = accountTable
      .filter(_.id === replenishAccount.id)
      .map(_.balance)
    for {
      existed <- db.run(query.result.headOption)
      _ <- db.run {
        if (replenishAccount.amount <= 0) {query.update(existed.get)}
        else {query.update(existed.get + replenishAccount.amount)}
      }
      res <- find(replenishAccount.id)
    } yield res
  }


  /**
   * Withdraws money from an existing account.
   *
   * @param withdrawAccount a JSON object containing the request to withdraw from an account, along with the amount
   *                        to withdraw
   * @return a Future containing the updated account, or None if no such account exists or if the amount to be
   *         withdrawn is less than or equal to zero or greater than the current balance of the account
   */
  override def withdrawAccount(withdrawAccount: WithdrawAccount): Future[Option[Account]] = {
    val query = accountTable
      .filter(_.id === withdrawAccount.id)
      .map(_.balance)
    for {
      existed <- db.run(query.result.headOption)
      _ <- db.run {
        if (withdrawAccount.amount <= 0 || withdrawAccount.amount > existed.get) {query.update(existed.get)}
        else {query.update(existed.get - withdrawAccount.amount)}
      }
      res <- find(withdrawAccount.id)
    } yield res
  }

  /**
   * Transfers money from one account to another.
   *
   * @param transferMoney a JSON object containing the request to transfer money from one account to another, along
   *                      with the unique identifiers of the accounts and the amount to transfer
   * @return a Future containing the updated account from which the money is transferred, or None if no such account
   *         exists or if the amount to be transferred is less than or equal to zero or greater than the current
   *         balance of the account
   */
  override def transferMoney(transferMoney: TransferMoney): Future[Option[Account]] = {
    val WithdrawFromAccount = WithdrawAccount(transferMoney.idFrom, transferMoney.amount)
    val queryFrom = accountTable
      .filter(_.id === transferMoney.idFrom)
      .map(_.balance)
    val queryTo = accountTable
      .filter(_.id === transferMoney.idTo)
      .map(_.balance)
    for {
      existedFrom <- db.run(queryFrom.result.headOption)
      existedTo <- db.run(queryTo.result.headOption)
      _ <- db.run {
        if (transferMoney.amount <= 0 || transferMoney.amount > existedFrom.get) {queryFrom.update(existedFrom.get)}
        else {
          withdrawAccount(WithdrawFromAccount)
          queryTo.update(existedTo.get + transferMoney.amount)
        }
      }
      res <- find(transferMoney.idFrom)
    } yield res
  }
  /**
   * Deletes an existing account.
   *
   * @param id the unique identifier of the account to delete
   * @return a Future containing Unit when the account is deleted
   */
  override def deleteAccount(id: UUID): Future[Unit] = {
    db.run(accountTable.filter(_.id === id).delete).map(_ => ())
  }
}