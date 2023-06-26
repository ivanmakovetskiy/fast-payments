package fastpayments
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import fastpayments.db.InitDB
import fastpayments.repository.AccountRepositoryDB
import fastpayments.route._
import slick.jdbc.PostgresProfile
import slick.jdbc.PostgresProfile.api._

import scala.concurrent
import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}

object FastPaymentsApp extends App with FailFastCirceSupport {
  /**
   * An implicit `ActorSystem` instance is required for running the HTTP server.
   */
  implicit val system: ActorSystem = ActorSystem("FastPaymentsApp")

  /**
   * An implicit `ExecutionContextExecutor` instance is required for running futures.
   */
  implicit val ec: ExecutionContextExecutor = system.dispatcher

  /**
   * An implicit `Database` instance is required for interacting with the database.
   */
  implicit val db: PostgresProfile.backend.Database = Database.forConfig("database.postgres")

  /**
   * Creates the database schema if it does not already exist.
   */
  new InitDB().prepare()

  /**
   * Create an instance of the AccountRepositoryDB using the provided ExecutionContext
   */
  val repository = new AccountRepositoryDB(?: ExecutionContext, $conforms)

  /**
   * Create an instance of the CategoryRepositoryDB using the provided ExecutionContext
   */
  val category = new CategoryRepositoryDB(?: ExecutionContext, $conforms)

  /**
   * Instantiates a new `HelloRoute` instance and binds it to the `helloRoute` variable.
   */
  private val helloRoute = new HelloRoute().route

  /**
   * Instantiates a new `AccountRoute` instance and binds it to the `accountRoute` variable.
   */
  private val accountRoute = new AccountRoute(repository, category).route

  /**
   * Creates a new HTTP server at the specified host and port and binds it to the `helloRoute` and `accountRoute`
   * variables.
   */
  Http().newServerAt("0.0.0.0", port = 8081).bind(helloRoute ~ accountRoute)
}