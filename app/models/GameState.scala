package models

import play.api.libs.json.Json

case class GameState(
  opponentName: String,
  pointsToWin: Int,
  maxRounds: Int,
  dynamiteCount: Int,
  round: Int = 1,
  plays: Seq[Play] = Nil
)

object GameState {
  val key = "game-state"

  implicit val formats = Json.format[GameState]
}