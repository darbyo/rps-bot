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
    Ok(new File("./logs/application.log").toPath.toAbsolutePath.toString)
  }

  def get(name: String) = Action {
    Ok.sendFile(new File(s"./logs/$name"), inline = true)
      .withHeaders(
        CACHE_CONTROL -> "max-age=3600",
        CONTENT_DISPOSITION -> s"attachment; filename=$name",
        CONTENT_TYPE -> "application/x-download"
      )
  }
}
