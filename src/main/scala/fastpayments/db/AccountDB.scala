package fastpayments.db
import slick.jdbc.PostgresProfile.api._
import fastpayments.model.Account
import java.util.UUID

object AccountDB {
  /**
   * The `AccountTable` class represents a database table for storing accounts.
   *
   * @param tag the `Tag` for the table
   */
    class AccountTable(tag: Tag) extends Table[Account](tag, "accounts"){
    /**
     * The unique identifier of the account.
     */
    val id = column[UUID]("id", O.PrimaryKey)
    val username = column[String]("username")
    /**
     * The balance of the account.
     */
    val balance = column[Float]("balance")

    /**
     * Maps a row of the table to an `Account` case class.
     */
    override def * = (id, username, balance) <> ((Account.apply _).tupled, Account.unapply _)
    }
  /**
   * The `accountTable` value represents the `AccountTable` as a `TableQuery`.
   */
    val accountTable = TableQuery[AccountTable]
}
