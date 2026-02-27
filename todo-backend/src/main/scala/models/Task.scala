package models

import com.greenfossil.commons.json.{Json, JsValue}
import db.TaskRow

case class TaskResponse(
  id: Long,
  title: String,
  description: Option[String],
  completed: Boolean,
  createdAt: String,
  updatedAt: String
)

object TaskResponse:
  def fromRow(row: TaskRow): TaskResponse =
    TaskResponse(
      id = row.id,
      title = row.title,
      description = row.description,
      completed = row.completed,
      createdAt = row.createdAt.toString,
      updatedAt = row.updatedAt.toString
    )

  def toJson(task: TaskResponse): JsValue =
    Json.obj(
      "id" -> task.id,
      "title" -> task.title,
      "description" -> task.description.orNull,
      "completed" -> task.completed,
      "createdAt" -> task.createdAt,
      "updatedAt" -> task.updatedAt
    )
