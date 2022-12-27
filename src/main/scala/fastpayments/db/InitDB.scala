package fastpayments.db
import fastpayments.db.AccountDB._
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.{ExecutionContext, Future}

class InitDB(implicit val ec: ExecutionContext, db: Database) {
  /**
   * Prepares the database by creating the `accounts` table if it does not already exist.
   *
   * @return a Future containing Unit when the table is created
   */
  def prepare(): Future[_] = {
    db.run(accountTable.schema.createIfNotExists)
  }
}