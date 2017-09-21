package services

import com.google.inject.{ImplementedBy, Inject}
import guesstimater.Guesstimater
import models.Move.Move
import models.{GameState, Result}
import utils.Logs

@ImplementedBy(classOf[CGuesstimaterService])
trait GuesstimaterService {
  val guesstimaters: Seq[Guesstimater]

  def updateCurrentGuesstimater(): Unit
  def getCurrentGuesstimater(gameState: GameState): Guesstimater
  def getGuess: Move
}

class CGuesstimaterService @Inject() (gameStateService: GameStateService, gs: Guesstimaters) extends GuesstimaterService {
  val guesstimaters: List[Guesstimater] = gs.get

  def updateCurrentGuesstimater(): Unit = {
    val gameState = gameStateService.getState()
    checkForLosingAll(gameState)
  }

  def getCurrentGuesstimater(gameState: GameState) = getGuesstimater(gameState.currentGuesstimater)
  private def getGuesstimater(index: Int) = guesstimaters(index % guesstimaters.length)

  def getGuess: Move = getCurrentGuesstimater(gameStateService.getState()).getGuess

  private def checkForLosingAll(gameState: GameState) = if(lostN(gameState, 5) == 5) updateState(gameState) else checkForLosingMost(gameState)
  private def checkForLosingMost(gameState: GameState) = if(lostN(gameState, 10) >= 6) updateState(gameState) else checkForDraws(gameState)
  private def checkForDraws(gameState: GameState) = if(lostN(gameState, 14) >= 7) updateState(gameState)

  private def updateState(gameState: GameState) = {
    gameStateService.setCurrentGuesstimater(gameState.currentGuesstimater + 1)
    gameStateService.setLastUpdateGuesstimater(gameState.round)

    Logs.guesstimaterUpdated(
      gameState,
      getGuesstimater(gameState.currentGuesstimater),
      getGuesstimater(gameState.currentGuesstimater + 1)
    )
  }

  private def lostN(gameState: GameState, lastN: Int) =
    gameState
      .plays
      .take(takeLower(lastN, gameState.round - gameState.lastUpdateGuesstimater))
      .count(!_.result.contains(Result.WIN))

  private def takeLower(x: Int, y: Int): Int = if (x < y) x else y
}