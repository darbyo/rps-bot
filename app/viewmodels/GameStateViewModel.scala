package viewmodels

import models.GameState
import play.api.libs.json.Json

case class GameStateViewModel(
                      opponentName: String,
                      pointsToWin: Int,
                      maxRounds: Int,
                      dynamiteCount: Int
                    )

object GameStateViewModel {
  implicit val formats = Json.format[GameStateViewModel]
  def asGameState(m: GameStateViewModel) = GameState(m.opponentName, m.pointsToWin, m.maxRounds, m.dynamiteCount)
}