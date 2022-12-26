package fastpayments.repository

import fastpayments.model._
import java.util.UUID
import scala.collection.mutable

import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}

class AccountRepositoryMutable(implicit val ec: ExecutionContext) extends AccountRepository {
  /**
   * The underlying storage for the accounts.
   */
  private val vault = mutable.Map[UUID, Account]()

  /**
   * Returns a sequence of all accounts.
   *
   * @return a Future containing a sequence of all accounts
   */
  override def list(): Future[Seq[Account]] = Future {
    vault.values.toList
  }

  /**
   * Returns the account with the given id.
   *
   * @param id the unique identifier of the account to retrieve
   * @return a Future containing the account with the given id, or an empty Future if no such account exists
   */
  override def get(id: UUID): Future[Account] = Future {
    vault(id)
  }

  /**
   * Creates a new account.
   *
   * @param create the request to create a new account
   * @return a Future containing the created account
   */
  override def createAccount(create: CreateAccount): Future[Account] = Future {
    val account = Account(id = UUID.randomUUID(), balance = 0)
    vault.put(account.id, account)
    account
  }

  /**
   * Replenishes the balance of an existing account.
   *
   * @param replenish the request to replenish an account, along with the amount to add to its balance
   * @return a Future containing the updated account, or an empty Future if no such account exists
   */
  override def replenishAccount(replenish: ReplenishAccount): Future[Option[Account]] = Future {
    vault.get(replenish.id).map { account =>
      val replenished = account.copy(balance = account.balance + replenish.amount)
      vault.put(account.id, replenished)
      replenished
    }
  }

  /**
   * Withdraws money from an existing account.
   *
   * @param withdraw the request to withdraw from an account, along with the amount to withdraw
   * @return a Future containing the updated account, or an empty Future if no such account exists or if the account
   *         does not have sufficient funds
   */
  override def withdrawAccount(withdraw: WithdrawAccount): Future[Option[Account]] = Future {
    vault.get(withdraw.id).map { account =>
      if (account.balance < withdraw.amount) {
        val withdrawed = account.copy()
        vault.put(account.id, withdrawed)
        withdrawed
      }
      else {
        val withdrawed = account.copy(balance = account.balance - withdraw.amount)
        vault.put(account.id, withdrawed)
        withdrawed
      }
    }
  }

  /**
   * Transfers money from one account to another.
   *
   * @param transfer the request to transfer money between accounts, along with the amount to transfer
   * @return a Future containing the updated account that the money was transferred from, or an empty Future if either
   *         of the accounts does not exist or if the account the money is transferred from does not have sufficient
   *         funds
   */
  override def transferMoney(transfer: TransferMoney): Future[Option[Account]] = Future {
    vault.get(transfer.idFrom).map { account =>
      if (account.balance < transfer.amount) {
        val transferred = account.copy()
        vault.put(account.id, transferred)
        transferred
      }
      else {
        val transferred = account.copy(balance = account.balance - transfer.amount)
        vault.put(account.id, transferred)
        vault.get(transfer.idTo).map { account =>
          val transferred = account.copy(balance = account.balance + transfer.amount)
          vault.put(account.id, transferred)
        }
        transferred
      }
    }
  }

  /**
   * Deletes the account with the given id.
   *
   * @param id the unique identifier of the account to delete
   * @return a Future containing Unit if the deletion was successful, or an empty Future if no such account exists
   */
  override def deleteAccount(id: UUID) = Future {
    vault.remove(id)
  }
}