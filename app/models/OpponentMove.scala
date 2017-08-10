package models

import models.Move.Move
import play.api.libs.json.Json

case class OpponentMove(opponentLastMove: Move)

object OpponentMove {
  implicit val formats = Json.format[OpponentMove]
}