package services

import models.{GameState, Move, Play}
import org.mockito.Matchers._
import org.mockito.Matchers.{eq => meq}
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfterEach, WordSpec}
import org.scalatest.Matchers._
import play.api.cache.CacheApi


class GameStateServiceSpec extends WordSpec with MockitoSugar with BeforeAndAfterEach {
  val mockCacheApi = mock[CacheApi]

  def serviceUnderTest = new CacheGameStateService(mockCacheApi)

  override def beforeEach = {
    reset(mockCacheApi)
  }

  "saveState" should {
    "persist state to cache with game-state key" in {
      val gameState = new GameState("", 0, 0, 0)
      serviceUnderTest.saveState(gameState)

      verify(mockCacheApi, times(1)).set(meq(GameState.key), any(), any())
    }

    "persist game state to cache" in {
      val gameState = new GameState("opponent 1", 0 , 0, 0)
      serviceUnderTest.saveState(gameState)

      verify(mockCacheApi, times(1)).set(any(), meq(gameState), any())
    }

    "persist game state with list of plays" in {
      val gameState = new GameState("opponent 2", 1, 1, 1, 1, Seq(Play(Move.ROCK)))
      serviceUnderTest.saveState(gameState)

      verify(mockCacheApi, times(1)).set(any(), meq(gameState), any())
    }
  }

  "getState" should {
    "return game state from cache" when {
      "game state exists in cache" in {
        val gameState = new GameState("opponent 1", 0, 0, 0)
        when(mockCacheApi.get[GameState](meq(GameState.key))(any())).thenReturn(Some(gameState))

        val result = serviceUnderTest.getState()
        result shouldBe gameState
      }

      "game state has list of plays" in {
        val gameState = new GameState("opponent 2", 1, 1, 1, 1, Seq(Play(Move.ROCK)))
        when(mockCacheApi.get[GameState](meq(GameState.key))(any())).thenReturn(Some(gameState))

        val result = serviceUnderTest.getState()
        result shouldBe gameState
      }
    }

    "throw NoSuchElementException" when {
      "game state not in cache" in {
        when(mockCacheApi.get[GameState](meq(GameState.key))(any())).thenReturn(None)

        val result = intercept[NoSuchElementException] {
          serviceUnderTest.getState()
        }

        result.getMessage shouldBe "Game state not in cache"
      }
    }
  }
}