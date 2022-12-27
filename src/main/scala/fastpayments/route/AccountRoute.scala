package fastpayments.route

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import fastpayments.model._
import fastpayments.repository.AccountRepository
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
class AccountRoute {
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
        /**
         * Returns the account with the given id.
         *
         * @param id the unique identifier of the account to retrieve
         * @return a JSON object containing the account
         */
        path("account" / JavaUUID) { id =>
          get {
            complete(repository.get(id))
          }
        } ~

        /**
         * Creates a new account.
         *
         * @param newAccount a JSON object containing the request to create a new account
         * @return a JSON object containing the created account
         */
        path("account" / "create") {
          (post & entity(as[CreateAccount])) { newAccount =>
            complete(repository.createAccount(newAccount))
          }
        } ~
          /**
           * Replenishes the balance of an existing account.
           *
           * @param replenishAccount a JSON object containing the request to replenish an account, along with the amount to
           *                         add to its balance
           * @return a JSON object containing the updated account, or an empty JSON object if no such account exists
           */
        path("account" / "replenish") {
          (put & entity(as[ReplenishAccount])) { replenishAccount =>
            complete(repository.replenishAccount(replenishAccount))
          }
        } ~

          /**
           * Withdraws money from an existing account.
           *
           * @param withdrawAccount a JSON object containing the request to withdraw from an account, along with the amount
           *                        to withdraw
           * @return a JSON object containing the updated account, or an empty JSON object if no such account exists or if
           *         the account does not have sufficient funds
           */
        path("account" / "withdraw") {
          (put & entity(as[WithdrawAccount])) { withdrawAccount =>
            complete(repository.withdrawAccount(withdrawAccount))
          }
        } ~
          /**
           * Transfers money from one account to another.
           *
           * @param transferMoney a JSON object containing the request to transfer money between accounts, along with the
           *                      amount to transfer
           * @return a JSON object containing the updated account that the money was transferred from, or an empty JSON
           *         object if either of the accounts does not exist or if the account the money is transferred from does
           *         not have sufficient funds
           */
        path("account" / "transfer") {
          (put & entity(as[TransferMoney])) { transferMoney =>
            complete(repository.transferMoney(transferMoney))
          }
        } ~
          /**
           * Deletes the account with the given id.
           *
           * @param id the unique identifier of the account to delete
           * @return an empty JSON object if the deletion was successful, or an empty JSON object if no such account exists
           */
        path("account" / JavaUUID) { id =>
          delete {
            complete(repository.deleteAccount(id))
          }
        }
  }
}
