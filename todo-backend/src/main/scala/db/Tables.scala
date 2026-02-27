package db

import slick.jdbc.MySQLProfile.api.*
import java.time.LocalDateTime

case class UserRow(
  id: Long = 0L,
  email: String,
  passwordHash: String,
  name: String,
  createdAt: LocalDateTime = LocalDateTime.now()
)

case class TaskRow(
  id: Long = 0L,
  userId: Long,
  title: String,
  description: Option[String] = None,
  completed: Boolean = false,
  createdAt: LocalDateTime = LocalDateTime.now(),
  updatedAt: LocalDateTime = LocalDateTime.now()
)

class UsersTable(tag: Tag) extends Table[UserRow](tag, "users"):
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def email = column[String]("email", O.Unique, O.Length(255))
  def passwordHash = column[String]("password_hash", O.Length(255))
  def name = column[String]("name", O.Length(255))
  def createdAt = column[LocalDateTime]("created_at")
  def * = (id, email, passwordHash, name, createdAt).mapTo[UserRow]

class TasksTable(tag: Tag) extends Table[TaskRow](tag, "tasks"):
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def userId = column[Long]("user_id")
  def title = column[String]("title", O.Length(255))
  def description = column[Option[String]]("description", O.Length(2000))
  def completed = column[Boolean]("completed", O.Default(false))
  def createdAt = column[LocalDateTime]("created_at")
  def updatedAt = column[LocalDateTime]("updated_at")
  def userFk = foreignKey("fk_tasks_user", userId, Tables.users)(_.id, onDelete = ForeignKeyAction.Cascade)
  def * = (id, userId, title, description, completed, createdAt, updatedAt).mapTo[TaskRow]

object Tables:
  val users = TableQuery[UsersTable]
  val tasks = TableQuery[TasksTable]
