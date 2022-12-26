package fastpayments

import SFP.model._
import SFP.repository.AccountRepositoryMutable
object FastPaymentsApp extends App  {
  val repository = new AccountRepositoryInMemory
  val AccIdFirst = repository.createAccount(CreateAccount()).id
  val AccIdSecond = repository.createAccount(CreateAccount()).id

  repository.replenishAccount(ReplenishAccount(AccIdFirst, 862))
  repository.replenishAccount(ReplenishAccount(AccIdSecond, 1103))
  println(repository.list())

  repository.withdrawAccount(WithdrawAccount(AccIdFirst, 1362))
  repository.withdrawAccount(WithdrawAccount(AccIdFirst, 362))
  repository.withdrawAccount(WithdrawAccount(AccIdSecond, 1362))
  repository.withdrawAccount(WithdrawAccount(AccIdSecond, 103))
  println(repository.list())

  repository.transferMoney(TransferMoney(AccIdFirst, AccIdSecond, 1500))
  repository.transferMoney(TransferMoney(AccIdFirst, AccIdSecond, 500))
  println(repository.list())
  repository.transferMoney(TransferMoney(AccIdSecond, AccIdFirst, 2500))
  repository.transferMoney(TransferMoney(AccIdSecond, AccIdFirst, 1500))
  println(repository.list())

  repository.deleteAccount(AccIdFirst)
  repository.deleteAccount(AccIdSecond)
  println(repository.list())
}
