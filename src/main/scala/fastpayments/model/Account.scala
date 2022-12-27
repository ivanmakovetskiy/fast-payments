package fastpayments.model
import java.util.UUID

/**
 * Represents an account in a bank.
 *
 * @param id the unique identifier for this account
 * @param balance the current balance of this account
 */
case class Account(id: UUID = UUID.randomUUID(), balance: Int = 0)

/**
 * Represents a request to create a new account.
 */
case class CreateAccount()

/**
 * Represents a request to add money to an account.
 *
 * @param id the unique identifier of the account to add money to
 * @param amount the amount of money to add to the account
 */
case class ReplenishAccount(id: UUID, amount: Int)

/**
 * Represents a request to withdraw money from an account.
 *
 * @param id the unique identifier of the account to withdraw money from
 * @param amount the amount of money to withdraw from the account
 */
case class WithdrawAccount(id: UUID, amount: Int)

/**
 * Represents a request to transfer money from one account to another.
 *
 * @param idFrom the unique identifier of the account to transfer money from
 * @param idTo the unique identifier of the account to transfer money to
 * @param amount the amount of money to transfer
 */
case class TransferMoney(idFrom: UUID, idTo: UUID, amount: Int)
