package services

import com.google.inject.{ImplementedBy, Inject}
import models.{GameState, Play, Result}
import models.Move.Move
import play.api.cache.CacheApi


@ImplementedBy(classOf[CacheGameStateService])
trait GameStateService {
  def saveState(gameState: GameState): Unit
  def getState(): GameState

  def addOurMove(move: Move): Unit
  def addOpponentMove(move: Move): Unit

  def setCurrentGuesstimater(n: Int): Unit
  def setLastUpdateGuesstimater(n: Int): Unit
}

class CacheGameStateService @Inject() (cacheApi: CacheApi) extends GameStateService {
  def saveState(gameState: GameState) = cacheApi.set(GameState.key, gameState)
  def getState() = cacheApi.get[GameState](GameState.key) match {
    case Some(gs) => gs
    case _ => throw new NoSuchElementException("Game state not in cache")
  }

  def addOurMove(move: Move): Unit = {
    val state = getState()
    saveState(state.copy(plays = state.plays :+ Play(move)))
  }

  def addOpponentMove(move: Move): Unit = {
    val state = getState()
    val lastPlay = state.plays.last

    if (lastPlay.opponentMove.isDefined || lastPlay.result.isDefined)
      throw new NoSuchElementException("Unable to find last play")

    val newPlay = Play(lastPlay.ourMove, Some(move), Some(Result.fromPlay(lastPlay.ourMove, move)))
    val round = state.round + 1
    val newState = state.copy(round = round, plays = state.plays.init :+ newPlay)

    saveState(newState)
  }

  def setCurrentGuesstimater(n: Int): Unit = {
    val gameState = getState()
    val newGameState = gameState.copy(currentGuesstimater = n)
    saveState(newGameState)
  }

  def setLastUpdateGuesstimater(n: Int): Unit = {
    val gameState = getState()
    val newGameState = gameState.copy(lastUpdateGuesstimater = n)
    saveState(newGameState)
  }
}