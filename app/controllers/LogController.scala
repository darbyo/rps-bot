package controllers

import java.io.File

import com.google.inject.Inject
import play.api.http.FileMimeTypes
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import utils.Logs

import scala.concurrent.ExecutionContext.Implicits.global

class LogController @Inject() (implicit fileMimeTypes: FileMimeTypes) extends Controller {
  def list = Action {
    Ok(Json.toJson(Logs.list))
  }

  def get(name: String) = Action {
    Ok.sendFile(new File(s"./logs/$name"), inline = true)
      .withHeaders(
        CACHE_CONTROL -> "max-age=3600",
        CONTENT_DISPOSITION -> "attachment; filename=abc.csv",
        CONTENT_TYPE -> "application/x-download"
      )
  }
}