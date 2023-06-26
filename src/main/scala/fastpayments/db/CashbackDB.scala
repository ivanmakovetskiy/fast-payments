package fastpayments.db

import fastpayments.model.Category
import slick.jdbc.PostgresProfile.api._
import java.util.UUID

/**
 * The `CashbackDB` object provides access to the database table for storing cashback categories.
 */
object CashbackDB {
  /**
   * The `CategoryTable` class represents a database table for storing cashback categories.
   *
   * @param tag The `Tag` for the table.
   */
  class CategoryTable(tag: Tag) extends Table[Category](tag, "categories") {
    /**
     * The unique identifier of the category.
     */
    val id = column[UUID]("id", O.PrimaryKey)

    /**
     * The name of the category.
     */
    val name = column[String]("name")

    /**
     * The cashback percentage associated with the category.
     */
    val percent = column[Float]("percent")

    /**
     * Maps a row of the table to a `Category` case class.
     */
    override def * = (id, name, percent) <> ((Category.apply _).tupled, Category.unapply _)
  }

  /**
   * The `cashbackTable` value represents the `CategoryTable` as a `TableQuery`.
   */
  val cashbackTable = TableQuery[CategoryTable]
}
