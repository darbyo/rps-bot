package services

import com.google.inject.{ImplementedBy, Inject}
import guesstimater.Guesstimater
import models.{GameState, Move, Result}

@ImplementedBy(classOf[CacheGuesstimaterService])
trait GuesstimaterService {
  val guesstimaters: Seq[Guesstimater]

  def updateCurrentGuesstimater(): Unit
  def getCurrentGuesstimater(gameState: GameState): Guesstimater
}

class CacheGuesstimaterService @Inject() (gameStateService: GameStateService) extends GuesstimaterService {
  val guesstimaters: Seq[Guesstimater] = Nil

  def updateCurrentGuesstimater(): Unit = {
    val gameState = gameStateService.getState()

    checkForLosingAll(gameState)
    checkForLosingMost(gameState)
    checkForDraws(gameState)
  }

  def getCurrentGuesstimater(gameState: GameState) =
    guesstimaters(gameState.currentGuesstimater % guesstimaters.length)

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