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
  implicit val system: ActorSystem = ActorSystem("FastPaymentsApp")
  implicit val ec: ExecutionContextExecutor = system.dispatcher
  implicit val db: PostgresProfile.backend.Database = Database.forConfig("database.postgres")

  new InitDB().prepare()
  val repository = new AccountRepositoryDB
  private val helloRoute = new HelloRoute().route
  private val accountRoute = new AccountRoute(repository).route
  Http().newServerAt("0.0.0.0", port = 8081).bind(helloRoute ~ accountRoute)
}