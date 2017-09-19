package models

import models.Move.Move
import models.Result.Result
import play.api.libs.json.Json

case class Play(ourMove: Move, opponentMove: Option[Move] = None, result: Option[Result] = None) {
  override def toString: String = s"($ourMove :: ${opponentMove.getOrElse("None")} :: ${result.getOrElse("None")})"
}

object Play {
  implicit val formats = Json.format[Play]
}