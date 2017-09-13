package models

import play.api.libs.json.Json

case class GameState(
  opponentName: String,
  pointsToWin: Int,
  maxRounds: Int,
  dynamiteCount: Int,
  round: Int = 1,
  plays: List[Play] = Nil,
  currentGuesstimater: Int = 0,
  lastUpdateGuesstimater: Int = 1
) {
  override def toString: String =
    s"""
       |  opponentName  = $opponentName
       |  pointsToWin   = $pointsToWin
       |  maxRounds     = $maxRounds
       |  dynamiteCount = $dynamiteCount
       |  round         = $round
       |  plays         = [
       |    ${plays.map(_.toString + "\n")}
       |  ]
       |  currentGuesstimater = $currentGuesstimater
       |  lastUpdateGuesstimater = $lastUpdateGuesstimater
     """.stripMargin
}

object GameState {
  val key = "game-state"

  implicit val formats = Json.format[GameState]
}