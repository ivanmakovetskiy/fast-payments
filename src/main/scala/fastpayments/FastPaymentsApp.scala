package fastpayments
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import fastpayments.db.InitDB
import fastpayments.repository.AccountRepositoryDB
import fastpayments.route._
import slick.jdbc.PostgresProfile
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.ExecutionContextExecutor

object FastPaymentsApp extends App {
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
   * Instantiates a new `AccountRepositoryDB` instance for interacting with the database.
   */
  val repository = new AccountRepositoryDB

  /**
   * Instantiates a new `HelloRoute` instance and binds it to the `helloRoute` variable.
   */
  private val helloRoute = new HelloRoute().route

  /**
   * Instantiates a new `AccountRoute` instance and binds it to the `accountRoute` variable.
   */
  private val accountRoute = new AccountRoute(repository).route

  /**
   * Creates a new HTTP server at the specified host and port and binds it to the `helloRoute` and `accountRoute`
   * variables.
   */
  Http().newServerAt("0.0.0.0", port = 8081).bind(helloRoute ~ accountRoute)
}