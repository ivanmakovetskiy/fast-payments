package fastpayments.model

import java.util.UUID

/**
 * Represents an account in a bank.
 *
 * @param id       The unique identifier for this account. If not provided, a random UUID will be generated.
 * @param username The username associated with the account.
 * @param balance  The current balance of this account.
 */
case class Account(
                    id: UUID = UUID.randomUUID(),
                    username: String,
                    balance: Float = 0
                  )

/**
 * Represents a request to create a new account.
 *
 * @param username The username for the new account.
 * @param balance  The initial balance for the new account.
 */
case class CreateAccount(
                          username: String,
                          balance: Float
                        )

/**
 * Represents a request to update an existing account.
 *
 * @param id       The unique identifier of the account.
 * @param username An optional new username for the account.
 * @param balance  An optional new balance for the account.
 */
case class UpdateAccount(
                          id: UUID,
                          username: Option[String] = None,
                          balance: Option[Float] = None
                        )
