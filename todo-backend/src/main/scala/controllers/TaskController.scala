package controllers

import com.greenfossil.thorium.{*, given}
import com.greenfossil.commons.json.{Json, JsArray, JsNull, JsObject, JsValue}
import com.greenfossil.data.mapping.Mapping.*
import com.linecorp.armeria.server.annotation.{Get, Post, Put, Delete, Param}

import auth.Authenticated
import models.TaskResponse
import services.TaskService

object TaskController:

  private val createTaskForm = tuple(
    "title" -> nonEmptyText,
    "description" -> optional[String]
  )

  private val updateTaskForm = tuple(
    "title" -> optional[String],
    "description" -> optional[String],
    "completed" -> optional[Boolean]
  )

  @Get("/api/tasks")
  def list = Action:
    implicit request =>
      Authenticated.withUser(request) { user =>
        val tasks = TaskService.findByUserId(user.id)
        val jsonTasks = tasks.map(t => TaskResponse.toJson(TaskResponse.fromRow(t)))
        Ok(Json.obj("tasks" -> Json.arr(jsonTasks*)))
      }

  @Post("/api/tasks")
  def create = Action:
    implicit request =>
      Authenticated.withUser(request) { user =>
        createTaskForm.bindFromRequest().fold(
          errorForm => BadRequest(Json.obj(
            "error" -> "VALIDATION_ERROR",
            "message" -> "Invalid input",
            "details" -> errorForm.errors.map(e => s"${e.key}: ${e.message}").mkString(", ")
          )),
          (title, description) =>
            TaskService.create(user.id, title, description).toEither match {
              case Left(errors) =>
                BadRequest(Json.obj(
                  "error" -> "VALIDATION_ERROR",
                  "message" -> "Invalid task data",
                  "details" -> errors.toList.mkString(", ")
                ))
              case Right(task) =>
                Ok(TaskResponse.toJson(TaskResponse.fromRow(task)))
            }
        )
      }

  @Get("/api/tasks/:id")
  def get(@Param id: Long) = Action:
    implicit request =>
      Authenticated.withUser(request) { user =>
        TaskService.findById(id, user.id) match
          case Some(task) =>
            Ok(TaskResponse.toJson(TaskResponse.fromRow(task)))
          case None =>
            NotFound(Json.obj(
              "error" -> "NOT_FOUND",
              "message" -> s"Task with id $id not found"
            ))
      }

  private def removeNulls(js: JsValue): JsValue = js match {
    case JsObject(fields) =>
      JsObject(fields.flatMap {
        case (_, JsNull) => None
        case (key, value) => Some(key -> removeNulls(value))
      }.toSeq)
    case JsArray(values) =>
      JsArray(values.map(removeNulls))
    case other => other
  }

  @Put("/api/tasks/:id")
  def update(@Param id: Long) = Action:
    implicit request =>
      Authenticated.withUser(request) { user =>
        updateTaskForm.bind(removeNulls(request.asJson)).fold(
          errorForm => BadRequest(Json.obj(
            "error" -> "VALIDATION_ERROR",
            "message" -> "Invalid input",
            "details" -> errorForm.errors.map(e => s"${e.key}: ${e.message}").mkString(", ")
          )),
          (title, description, completed) =>
            TaskService.update(id, user.id, title, description, completed).toEither match {
              case Left(errors) =>
                BadRequest(Json.obj(
                  "error" -> "VALIDATION_ERROR",
                  "message" -> "Invalid task data",
                  "details" -> errors.toList.mkString(", ")
                ))
              case Right(Some(task)) =>
                Ok(TaskResponse.toJson(TaskResponse.fromRow(task)))
              case Right(None) =>
                NotFound(Json.obj(
                  "error" -> "NOT_FOUND",
                  "message" -> s"Task with id $id not found"
                ))
            }
        )
      }

  @Delete("/api/tasks/:id")
  def delete(@Param id: Long) = Action:
    implicit request =>
      Authenticated.withUser(request) { user =>
        if TaskService.delete(id, user.id) then
          Ok(Json.obj("message" -> "Task deleted successfully"))
        else
          NotFound(Json.obj(
            "error" -> "NOT_FOUND",
            "message" -> s"Task with id $id not found"
          ))
      }
