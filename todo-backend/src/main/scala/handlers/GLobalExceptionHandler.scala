package handlers

import com.linecorp.armeria.common.{HttpRequest, HttpResponse, HttpStatus, MediaType}
import com.linecorp.armeria.server.{HttpService, ServiceRequestContext, SimpleDecoratingHttpService}
import com.fasterxml.jackson.databind.ObjectMapper
import exceptions.AppException
import scala.util.control.NonFatal

class GlobalExceptionHandler(delegate: HttpService) extends SimpleDecoratingHttpService(delegate) {
  private val objectMapper = new ObjectMapper()

  override def serve(ctx: ServiceRequestContext, req: HttpRequest): HttpResponse = {
    try {
      val response = delegate.serve(ctx, req)
      // Wrap the response to handle exceptions from async operations
      response.recover {
        case ex: AppException =>
          createErrorResponse(ex.httpStatus, getErrorName(ex), ex.getMessage)
        case _: Throwable =>
          createErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "INTERNAL_SERVER_ERROR",
            "An unexpected error occurred"
          )
      }
    } catch {
      case ex: AppException =>
        createErrorResponse(ex.httpStatus, getErrorName(ex), ex.getMessage)
      case NonFatal(_) =>
        createErrorResponse(
          HttpStatus.INTERNAL_SERVER_ERROR,
          "INTERNAL_SERVER_ERROR",
          "An unexpected error occurred"
        )
    }
  }

  private def getErrorName(ex: AppException): String = {
    ex match {
      case _: exceptions.NotFoundException => "NOT_FOUND"
      case _: exceptions.BadRequestException => "BAD_REQUEST"
      case _: exceptions.ConflictException => "CONFLICT"
      case _: exceptions.UnprocessableEntityException => "UNPROCESSABLE_ENTITY"
    }
  }

  private def createErrorResponse(status: HttpStatus, errorType: String, message: String): HttpResponse = {
    val errorResponse = ErrorResponse(
      error = errorType,
      message = message,
      timestamp = System.currentTimeMillis()
    )

    val json = objectMapper.writeValueAsString(errorResponse)
    HttpResponse.builder()
      .status(status)
      .content(MediaType.JSON, json)
      .build()
  }
}
