package fastpayments.repository

import fastpayments.model._
import java.util.UUID
import scala.concurrent.Future

/**
 * The `AccountRepository` trait provides methods for accessing and manipulating accounts.
 */
trait AccountRepository {
  /**
   * Returns a sequence of all accounts.
   *
   * @return A `Future` containing a sequence of all accounts.
   */
  def list(): Future[Seq[Account]]

  /**
   * Returns the account with the given id.
   *
   * @param id The unique identifier of the account to retrieve.
   * @return A `Future` containing the account with the given id, or an empty `Future` if no such account exists.
   */
  def get(id: UUID): Future[Account]

  /**
   * Creates a new account.
   *
   * @param item The account to create.
   * @return A `Future` containing the created account.
   */
  def create(item: CreateAccount): Future[Account]

  /**
   * Updates an existing account.
   *
   * @param item The updated account information.
   * @return A `Future` containing an `Option` with the updated account if it exists, or `None` otherwise.
   */
  def update(item: UpdateAccount): Future[Option[Account]]

  /**
   * Deletes an account with the given id.
   *
   * @param id The unique identifier of the account to delete.
   * @return A `Future` representing the completion of the deletion operation.
   */
  def delete(id: UUID): Future[Unit]
}
