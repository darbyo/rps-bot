package models

import play.api.libs.json.Json

case class GameStart(
  opponentName: String,
  pointsToWin: Int,
  maxRounds: Int,
  dynamiteCount: Int
)

object GameStart {
  implicit val formats = Json.format[GameStart]
}