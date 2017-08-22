package services

import com.google.inject.{ImplementedBy, Inject}
import models.{GameState, Result}

@ImplementedBy(classOf[CacheGuesstimaterService])
trait GuesstimaterService {
  def updateCurrentGuesstimater(): Unit
}

class CacheGuesstimaterService @Inject() (gameStateService: GameStateService) extends GuesstimaterService {
  def updateCurrentGuesstimater(): Unit = {
    val gameState = gameStateService.getState()

    checkForLosingAll(gameState)
    checkForLosingMost(gameState)
    checkForDraws(gameState)
  }

  private def checkForLosingAll(gameState: GameState) = if(lostN(gameState, 5) == 5) updateState(gameState)
  private def checkForLosingMost(gameState: GameState) = if(lostN(gameState, 10) == 6) updateState(gameState)
  private def checkForDraws(gameState: GameState) = if(lostN(gameState, 14) == 7) updateState(gameState)

  private def updateState(gameState: GameState) = {
    gameStateService.setCurrentGuesstimater(gameState.currentGuesstimater + 1)
    gameStateService.setLastUpdateGuesstimater(gameState.round)
  }

  private def lostN(gameState: GameState, lastN: Int) = {
    gameState.plays.drop(gameState.lastUpdateGuesstimater - 1).reverse.take(lastN).count(_.result.contains(Result.LOSE))
  }
}