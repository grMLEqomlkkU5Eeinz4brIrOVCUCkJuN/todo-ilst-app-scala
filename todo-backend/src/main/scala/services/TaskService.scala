package services

import cats.data.ValidatedNel
import cats.syntax.all.*
import db.{Database, Tables, TaskRow}
import slick.jdbc.MySQLProfile.api.*

import java.time.LocalDateTime
import scala.concurrent.Await
import scala.concurrent.duration.*

object TaskService:
  private val db = Database.db

  private def validateCreateData(title: String): ValidatedNel[String, String] =
    if title.nonEmpty then title.validNel
    else "Title cannot be empty".invalidNel

  def create(userId: Long, title: String, description: Option[String]): ValidatedNel[String, TaskRow] =
    validateCreateData(title).map { validTitle =>
      val now = LocalDateTime.now()
      val row = TaskRow(userId = userId, title = validTitle, description = description, createdAt = now, updatedAt = now)
      val insertAction = (Tables.tasks returning Tables.tasks.map(_.id) into ((task, id) => task.copy(id = id))) += row
      Await.result(db.run(insertAction), 10.seconds)
    }

  def findByUserId(userId: Long): Seq[TaskRow] =
    Await.result(
      db.run(Tables.tasks.filter(_.userId === userId).sortBy(_.createdAt.desc).result),
      10.seconds
    )

  def findById(id: Long, userId: Long): Option[TaskRow] =
    Await.result(
      db.run(Tables.tasks.filter(t => t.id === id && t.userId === userId).result.headOption),
      10.seconds
    )

  private def validateUpdateData(title: Option[String]): ValidatedNel[String, Option[String]] =
    title match
      case Some(t) if t.isEmpty => "Title cannot be empty".invalidNel
      case _ => title.validNel

  def update(id: Long, userId: Long, title: Option[String], description: Option[String], completed: Option[Boolean]): ValidatedNel[String, Option[TaskRow]] =
    validateUpdateData(title).map { validTitle =>
      val now = LocalDateTime.now()
      val query = Tables.tasks.filter(t => t.id === id && t.userId === userId)

      val updateActions = List(
        validTitle.map(t => query.map(_.title).update(t)),
        description.map(_ => query.map(_.description).update(description)),
        completed.map(c => query.map(_.completed).update(c))
      ).flatten

      if updateActions.isEmpty then
        findById(id, userId)
      else
        val updateTimestamp = query.map(_.updatedAt).update(now)
        val allUpdates = DBIO.seq((updateActions :+ updateTimestamp)*)
        Await.result(db.run(allUpdates.transactionally), 10.seconds)
        findById(id, userId)
    }

  def delete(id: Long, userId: Long): Boolean =
    val query = Tables.tasks.filter(t => t.id === id && t.userId === userId)
    val rowsDeleted = Await.result(db.run(query.delete), 10.seconds)
    rowsDeleted > 0
