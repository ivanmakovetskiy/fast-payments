package fastpayments.db

import fastpayments.db.AccountDB._
import fastpayments.CashbackDB.cashbackTable
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.{ExecutionContext, Future}

/**
 * The `InitDB` class is responsible for preparing the database for the application.
 *
 * @param ec The execution context for running asynchronous operations.
 * @param db The database instance to work with.
 */
class InitDB(implicit val ec: ExecutionContext, db: Database) {
  /**
   * Prepares the database by creating the `accounts` and `cashback` tables if they do not already exist.
   *
   * @return A `Future` containing `Unit` when the tables are created.
   */
  def prepare(): Future[_] = {
    db.run(accountTable.schema.createIfNotExists)
    db.run(cashbackTable.schema.createIfNotExists)
  }
}
