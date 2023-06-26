package fastpayments.model
import java.util.UUID

/**
 * Represents a cashback category.
 *
 * @param id      The unique identifier for the category. If not provided, a random UUID will be generated.
 * @param name    The name of the category.
 * @param percent The cashback percentage associated with the category.
 */
case class Category(id: UUID = UUID.randomUUID(), name: String, percent: Float)

/**
 * Represents the request to add a cashback category.
 *
 * @param name    The name of the category.
 * @param percent The cashback percentage associated with the category.
 */
case class AddCategory(name: String, percent: Float)

/**
 * Represents the request to update a cashback category.
 *
 * @param id      The unique identifier of the category.
 * @param name    An optional new name for the category.
 * @param percent An optional new cashback percentage for the category.
 */
case class UpdateCategory(id: UUID, name: Option[String] = None, percent: Option[Float] = None)