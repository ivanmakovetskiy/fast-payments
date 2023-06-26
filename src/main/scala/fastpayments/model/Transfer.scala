package fastpayments.model

import java.util.UUID

/**
 * Represents an item for replenishing an account.
 *
 * @param id     The unique identifier for the replenish item.
 * @param amount The amount to replenish the account.
 */
case class ReplenishItem(
                          id: UUID,
                          amount: Float
                        )

/**
 * Represents an item for withdrawing from an account.
 *
 * @param id     The unique identifier for the withdraw item.
 * @param amount The amount to withdraw from the account.
 */
case class WithdrawItem(
                         id: UUID,
                         amount: Int
                       )

/**
 * Represents an item for transferring funds between accounts.
 *
 * @param from        The unique identifier of the account to transfer funds from.
 * @param to          The unique identifier of the account to transfer funds to.
 * @param amount      The amount to transfer.
 * @param category_id An optional unique identifier of the category associated with the transfer.
 */
case class TransferItem(
                         from: UUID,
                         to: UUID,
                         amount: Int,
                         category_id: Option[UUID] = None
                       )

/**
 * Represents a response for a fund transfer between accounts.
 *
 * @param from The account from which funds were transferred.
 * @param to   The account to which funds were transferred.
 */
case class TransferResponse(
                             from: Account,
                             to: Account
                           )
