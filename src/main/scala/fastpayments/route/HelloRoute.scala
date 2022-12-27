package fastpayments.route

import akka.http.scaladsl.server.Directives._

class HelloRoute {
  def route =
    (path("hello") & get) {
      complete("!")
    }
}
