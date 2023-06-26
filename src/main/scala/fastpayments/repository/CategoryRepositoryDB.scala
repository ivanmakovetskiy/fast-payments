package fastpayments.repository

import fastpayments.model._
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._
import fastpayments.db.CashbackDB.cashbackTable
import fastpayments.repository.CategoryRepository

import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}

class CategoryRepositoryDB(implicit val ec: ExecutionContext, db: Database) extends CategoryRepository {

  // Returns a Future containing a sequence of all cashback categories
  override def list(): Future[Seq[Category]] = {
    db.run(cashbackTable.result)
  }

  // Creates a new cashback category and returns the created category
  override def create(add: AddCategory): Future[Category] = {
    val item = Category(name = add.name, percent = add.percent)
    for {
      _ <- db.run(cashbackTable += item)
      res <- get(item.id)
    } yield res
  }

  // Retrieves a cashback category with the given id
  override def get(id: UUID): Future[Category] = {
    db.run(cashbackTable.filter(_.id === id).result.head)
  }

  // Deletes a cashback category with the given id
  override def delete(id: UUID): Future[Unit] = Future {
    db.run(cashbackTable.filter(_.id === id).delete).map(_ => ())
  }

  // Finds a cashback category with the given id and returns an optional category
  def find(id: UUID): Future[Option[Category]] = {
    db.run(cashbackTable.filter(_.id === id).result.headOption)
  }

  // Updates an existing cashback category and returns an optional category
  override def update(item: UpdateCategory): Future[Option[Category]] = {
    for {
      _ <- db.run {
        cashbackTable.filter(_.id === item.id)
          .map(a => (a.name, a.percent))
          .update((item.name.getOrElse("None"), item.percent.getOrElse(0)))
      }
      res <- find(item.id)
    } yield res
  }
}
