package services

import com.google.inject.{ImplementedBy, Inject}
import models.GameState
import play.api.cache.CacheApi


@ImplementedBy(classOf[CacheGameStateService])
trait GameStateService {
  def saveState(gameState: GameState): Unit
  def getState(): GameState
}

class CacheGameStateService @Inject() (cacheApi: CacheApi) extends GameStateService {
  def saveState(gameState: GameState) = cacheApi.set(GameState.key, gameState)
  def getState() = cacheApi.get[GameState](GameState.key) match {
    case Some(gs) => gs
    case _ => throw new NoSuchElementException("Game state not in cache")
  }
}