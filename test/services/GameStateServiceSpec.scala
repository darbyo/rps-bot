package services

import models.{GameState, Move, Play, Result}
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
      val gameState = GameState("", 0, 0, 0)
      serviceUnderTest.saveState(gameState)

      verify(mockCacheApi, times(1)).set(meq(GameState.key), any(), any())
    }

    "persist game state to cache" in {
      val gameState = GameState("opponent 1", 0 , 0, 0)
      serviceUnderTest.saveState(gameState)

      verify(mockCacheApi, times(1)).set(any(), meq(gameState), any())
    }

    "persist game state with list of plays" in {
      val gameState = GameState("opponent 2", 1, 1, 1, 1, Seq(Play(Move.ROCK)))
      serviceUnderTest.saveState(gameState)

      verify(mockCacheApi, times(1)).set(any(), meq(gameState), any())
    }
  }

  "getState" should {
    "return game state from cache" when {
      "game state exists in cache" in {
        val gameState = GameState("opponent 1", 0, 0, 0)
        when(mockCacheApi.get[GameState](meq(GameState.key))(any())).thenReturn(Some(gameState))

        val result = serviceUnderTest.getState()
        result shouldBe gameState
      }

      "game state has list of plays" in {
        val gameState = GameState("opponent 2", 1, 1, 1, 1, Seq(Play(Move.ROCK)))
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

  "addOurMove" should {
    "add move to game state - test 1" in {
      val gameState = GameState("opponent 1", 500, 1000, 100, 1)
      when(mockCacheApi.get[GameState](meq(GameState.key))(any())).thenReturn(Some(gameState))

      serviceUnderTest.addOurMove(Move.ROCK)
      val expectedState = gameState.copy(plays = List(Play(Move.ROCK)))

      verify(mockCacheApi, times(1)).set(meq(GameState.key), meq(expectedState), any())
    }

    "add move to game state - test 2" in {
      val gameState = GameState("ABC123", 100, 200, 10, 2)
      when(mockCacheApi.get[GameState](meq(GameState.key))(any())).thenReturn(Some(gameState))

      serviceUnderTest.addOurMove(Move.PAPER)
      val expectedState = gameState.copy(plays = List(Play(Move.PAPER)))

      verify(mockCacheApi, times(1)).set(meq(GameState.key), meq(expectedState), any())
    }

    "append move to list - 1" in {
      val currentPlay = Play(Move.PAPER, Some(Move.ROCK), Some(Result.WIN))
      val gameState = GameState("opponent 1", 500, 1000, 100, 1, List(currentPlay))

      when(mockCacheApi.get[GameState](meq(GameState.key))(any())).thenReturn(Some(gameState))

      serviceUnderTest.addOurMove(Move.ROCK)
      val expectedState = gameState.copy(plays = List(currentPlay, Play(Move.ROCK)))

      verify(mockCacheApi, times(1)).set(meq(GameState.key), meq(expectedState), any())
    }
  }

  "addOpponentMove" should {
    "add move to game state - test 1" in {
      val gameState = GameState("opponent 1", 500, 1000, 100, 1, List(Play(Move.ROCK)))
      when(mockCacheApi.get[GameState](meq(GameState.key))(any())).thenReturn(Some(gameState))

      serviceUnderTest.addOpponentMove(Move.PAPER)
      val expectedState = gameState.copy(round = 2, plays = List(Play(Move.ROCK, Some(Move.PAPER), Some(Result.LOSE))))

      verify(mockCacheApi, times(1)).set(meq(GameState.key), meq(expectedState), any())
    }

    "add move to game state - test 2" in {
      val gameState = GameState("ABC123", 50, 100, 10, 4, List(Play(Move.PAPER)))
      when(mockCacheApi.get[GameState](meq(GameState.key))(any())).thenReturn(Some(gameState))

      serviceUnderTest.addOpponentMove(Move.ROCK)
      val expectedState = gameState.copy(round = 5, plays = List(Play(Move.PAPER, Some(Move.ROCK), Some(Result.WIN))))

      verify(mockCacheApi, times(1)).set(meq(GameState.key), meq(expectedState), any())
    }

    "append new play to plays" in {
      val lastPlay = Play(Move.ROCK, Some(Move.SCISSORS), Some(Result.WIN))
      val gameState = GameState("ABC123", 50, 100, 10, 4, List(lastPlay, Play(Move.PAPER)))
      when(mockCacheApi.get[GameState](meq(GameState.key))(any())).thenReturn(Some(gameState))

      serviceUnderTest.addOpponentMove(Move.ROCK)
      val expectedState = gameState.copy(round = 5, plays = List(lastPlay, Play(Move.PAPER, Some(Move.ROCK), Some(Result.WIN))))

      verify(mockCacheApi, times(1)).set(meq(GameState.key), meq(expectedState), any())
    }

    "throw exception if last play has opponent move" in {
      val gameState = GameState("opponent 1", 500, 1000, 100, 1, List(Play(Move.ROCK, Some(Move.ROCK))))
      when(mockCacheApi.get[GameState](meq(GameState.key))(any())).thenReturn(Some(gameState))

      intercept[NoSuchElementException]{
        serviceUnderTest.addOpponentMove(Move.PAPER)
      }
    }

    "throw exception if last play has result" in {
      val gameState = GameState("opponent 1", 500, 1000, 100, 1, List(Play(Move.ROCK, None, Some(Result.WIN))))
      when(mockCacheApi.get[GameState](meq(GameState.key))(any())).thenReturn(Some(gameState))

      intercept[NoSuchElementException]{
        serviceUnderTest.addOpponentMove(Move.PAPER)
      }
    }
  }
}