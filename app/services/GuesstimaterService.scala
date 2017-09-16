package services

import com.google.inject.{ImplementedBy, Inject}
import guesstimater.Guesstimater
import models.Move.Move
import models.{GameState, Result}
import play.api.Logger

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

  def getCurrentGuesstimater(gameState: GameState) =
    guesstimaters(gameState.currentGuesstimater % guesstimaters.length)

  def getGuess: Move = getCurrentGuesstimater(gameStateService.getState()).getGuess

  private def checkForLosingAll(gameState: GameState) = if(lostN(gameState, 5) == 5) updateState(gameState) else checkForLosingMost(gameState)
  private def checkForLosingMost(gameState: GameState) = if(lostN(gameState, 10) == 6) updateState(gameState) else checkForDraws(gameState)
  private def checkForDraws(gameState: GameState) = if(lostN(gameState, 14) == 7) updateState(gameState)

  private def updateState(gameState: GameState) = {
    gameStateService.setCurrentGuesstimater(gameState.currentGuesstimater + 1)
    gameStateService.setLastUpdateGuesstimater(gameState.round)

    Logger.logger.info(
      s"""
         |Round ${gameState.round}:
         |  New guesstimater: ${getCurrentGuesstimater(gameState)}
       """.stripMargin)
  }

  private def lostN(gameState: GameState, lastN: Int) =
    gameState
      .plays
      .drop(gameState.lastUpdateGuesstimater - 1)
      .take(lastN)
      .count(_.result.contains(Result.LOSE))
}