package fastpayments.repository

import fastpayments.model._

import java.util.UUID
import scala.concurrent.Future


trait AccountRepository {
  /**
   * Returns a sequence of all accounts.
   *
   * @return a Future containing a sequence of all accounts
   */
  def list(): Future[Seq[Account]]

  /**
   * Returns the account with the given id.
   *
   * @param id the unique identifier of the account to retrieve
   * @return a Future containing the account with the given id, or an empty Future if no such account exists
   */
  def get(id: UUID): Future[Account]

  /**
   * Creates a new account.
   *
   * @param account the account to create
   * @return a Future containing the created account
   */
  def createAccount(account: CreateAccount): Future[Account]
  def replenishAccount(account: ReplenishAccount): Future[Option[Account]]
  def withdrawAccount(account: WithdrawAccount): Future[Option[Account]]
  def transferMoney(account: TransferMoney): Future[Option[Account]]
  def deleteAccount(id: UUID): Future[Unit]
}