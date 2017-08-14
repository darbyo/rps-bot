package viewmodels

import models.Move.Move
import play.api.libs.json.Json

case class OpponentMove(lastOpponentMove: Move)

object OpponentMove {
  implicit val formats = Json.format[OpponentMove]
}