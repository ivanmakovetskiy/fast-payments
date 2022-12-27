package fastpayments.route

import akka.http.scaladsl.server.Directives._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import fastpayments.model._
import fastpayments.repository.AccountRepository
import io.circe.generic.auto._

  class AccountRoute(repository: AccountRepository) extends FailFastCirceSupport{
    /**
     * Returns a sequence of all accounts.
     *
     * @return a JSON array containing the accounts
     */
    def route =
      (path("accounts") & get) {
        val list = repository.list()
        complete(list)
      } ~
        path("account" / JavaUUID) { id =>
          get {
            complete(repository.get(id))
          }
        } ~
        path("account" / "create") {
          (post & entity(as[CreateAccount])) { newAccount =>
            complete(repository.createAccount(newAccount))
          }
        } ~
        path("account" / "replenish") {
          (put & entity(as[ReplenishAccount])) { replenishAccount =>
            complete(repository.replenishAccount(replenishAccount))
          }
        } ~
        path("account" / "withdraw") {
          (put & entity(as[WithdrawAccount])) { withdrawAccount =>
            complete(repository.withdrawAccount(withdrawAccount))
          }
        } ~
        path("account" / "transfer") {
          (put & entity(as[TransferMoney])) { transferMoney =>
            complete(repository.transferMoney(transferMoney))
          }
        } ~
        path("account" / JavaUUID) { id =>
          delete {
            complete(repository.deleteAccount(id))
          }
        }
}
