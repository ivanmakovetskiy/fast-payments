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

  /**
   * Replenishes the balance of an existing account.
   *
   * @param account the account to replenish, along with the amount to add to its balance
   * @return a Future containing the updated account, or an empty Future if no such account exists
   */
  def replenishAccount(account: ReplenishAccount): Future[Option[Account]]

  /**
   * Withdraws money from an existing account.
   *
   * @param account the account to withdraw from, along with the amount to withdraw
   * @return a Future containing the updated account, or an empty Future if no such account exists or if the account
   *         does not have sufficient funds
   */
  def withdrawAccount(account: WithdrawAccount): Future[Option[Account]]

  /**
   * Transfers money from one account to another.
   *
   * @param account the accounts involved in the transfer, along with the amount to transfer
   * @return a Future containing the updated account that the money was transferred from, or an empty Future if either
   *         of the accounts does not exist or if the account the money is transferred from does not have sufficient
   *         funds
   */
  def transferMoney(account: TransferMoney): Future[Option[Account]]

  /**
   * Deletes the account with the given id.
   *
   * @param id the unique identifier of the account to delete
   * @return a Future containing Unit if the deletion was successful, or an empty Future if no such account exists
   */
  def deleteAccount(id: UUID): Future[Unit]
}