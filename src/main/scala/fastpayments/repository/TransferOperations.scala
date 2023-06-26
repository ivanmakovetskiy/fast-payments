package fastpayments.repository

import fastpayments.model._
import scala.concurrent.Future

/**
 * The `TransferOperations` trait defines the operations related to transferring funds between accounts.
 */
trait TransferOperations {
  /**
   * Replenishes the balance of an account with the specified amount.
   *
   * @param replenishItem the replenish item containing the account id and amount
   * @return a Future containing an `Either` result, where `Right` represents the updated account and
   *         `Left` represents an error message if the replenishment failed
   */
  def replenish(replenishItem: ReplenishItem): Future[Either[String, Account]]

  /**
   * Withdraws funds from the account with the specified amount.
   *
   * @param withdrawItem the withdraw item containing the account id and amount
   * @return a Future containing an `Either` result, where `Right` represents the updated account and
   *         `Left` represents an error message if the withdrawal failed
   */
  def withdraw(withdrawItem: WithdrawItem): Future[Either[String, Account]]

  /**
   * Transfers funds from one account to another.
   *
   * @param transferItem the transfer item containing the source account id, destination account id,
   *                     amount, and optional category id
   * @return a Future containing an `Either` result, where `Right` represents the transfer response
   *         with the source and destination accounts, and `Left` represents an error message if the transfer failed
   */
  def transfer(transferItem: TransferItem): Future[Either[String, TransferResponse]]
}
