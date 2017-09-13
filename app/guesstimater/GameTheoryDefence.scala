package guesstimater

import com.google.inject.{ImplementedBy, Inject}
import models.{Play, Result}
import services.GameStateService

@ImplementedBy(classOf[CGameTheoryDefence])
trait GameTheoryDefence extends Guesstimater

class CGameTheoryDefence @Inject() (random: Random, gameState: GameStateService) extends GameTheoryDefence {
  def predict(play: Play) = {
    if(play.result.contains(Result.DRAW)) random.getGuess else play.opponentMove.get
  }

  def getGuess = predict(gameState.getLastPlay())
}