package fastpayments
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import fastpayments.db.InitDB
import fastpayments.repository.AccountRepositoryDB
import fastpayments.route._
import slick.jdbc.PostgresProfile.api._

object FastPaymentsDbApp extends App {
  implicit val system: ActorSystem = ActorSystem("SystemFastPaymentsApp")
  implicit val ec = system.dispatcher
  implicit val db = Database.forConfig("database.postgres")

  new InitDB().prepare()
  val repository = new AccountRepositoryDB
  val helloRoute = new HelloRoute().route
  val accountRoute = new AccountRoute(repository).route

  Http().newServerAt("0.0.0.0", port = 8080).bind(helloRoute ~ accountRoute)
}