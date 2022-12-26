package fastpayments.model

import java.util.UUID
case class Account(id: UUID = UUID.randomUUID(), balance: Int = 0)

/**
 * Represents an account in a bank.
 *
 * @param id the unique identifier for this account
 * @param balance the current balance of this account
 */

