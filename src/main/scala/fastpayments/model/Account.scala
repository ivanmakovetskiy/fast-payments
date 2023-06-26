package fastpayments.model

import java.util.UUID

/**
 * Represents an account in a bank.
 *
 * @param id      The unique identifier for this account. If not provided, a random UUID will be generated.
 * @param balance The current balance of this account.
 */
case class Account(
                    id: UUID = UUID.randomUUID(),
                    balance: Int = 0
                  )

/**
 * Represents a request to create a new account.
 */
case class CreateAccount()

/**
 * Represents a request to update an existing account.
 *
 * @param id       The unique identifier of the account.
 * @param username An optional new username for the account.
 * @param sum      An optional new sum for the account.
 */
case class UpdateAccount(
                          id: UUID,
                          username: Option[String] = None,
                          sum: Option[Float] = None
                        )
