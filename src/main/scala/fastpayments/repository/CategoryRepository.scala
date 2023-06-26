package fastpayments.repository
import java.util.UUID
import scala.concurrent.Future
import fastpayments.model._

/**
 * A repository for managing cashback categories.
 */
trait CategoryRepository {
  /**
   * Retrieves a list of all cashback categories.
   *
   * @return A Future containing a sequence of Category objects.
   */
  def list(): Future[Seq[Category]]

  /**
   * Retrieves a cashback category by its unique identifier.
   *
   * @param id The unique identifier of the category.
   * @return A Future containing the requested Category object.
   */
  def get(id: UUID): Future[Category]

  /**
   * Creates a new cashback category.
   *
   * @param item The AddCategory object containing the details of the category to be created.
   * @return A Future containing the created Category object.
   */
  def create(item: AddCategory): Future[Category]

  /**
   * Updates an existing cashback category.
   *
   * @param item The UpdateCategory object containing the details of the category to be updated.
   * @return A Future containing an optional Category object representing the updated category.
   *         The Future will be completed with None if the category was not found.
   */
  def update(item: UpdateCategory): Future[Option[Category]]

  /**
   * Deletes a cashback category by its unique identifier.
   *
   * @param id The unique identifier of the category to be deleted.
   * @return A Future indicating the completion of the deletion operation.
   */
  def delete(id: UUID): Future[Unit]
}