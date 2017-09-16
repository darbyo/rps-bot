package services

import models.{GameState, Move, Play, Result}
import org.mockito.Matchers._
import org.mockito.Matchers.{eq => meq}
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfterEach, WordSpec}
import org.scalatest.Matchers._
import play.api.cache.SyncCacheApi


class GameStateServiceSpec extends WordSpec with MockitoSugar with BeforeAndAfterEach {
  val mockCacheApi = mock[SyncCacheApi]

  def serviceUnderTest = new CGameStateService(mockCacheApi)

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
      val gameState = GameState("opponent 2", 1, 1, 1, 1, List(Play(Move.ROCK)))
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
        val gameState = GameState("opponent 2", 1, 1, 1, 1, List(Play(Move.ROCK)))
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
      val expectedState = gameState.copy(plays = List(Play(Move.ROCK), currentPlay))

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
      val gameState = GameState("ABC123", 50, 100, 10, 4, List(Play(Move.PAPER), lastPlay))
      when(mockCacheApi.get[GameState](meq(GameState.key))(any())).thenReturn(Some(gameState))

      serviceUnderTest.addOpponentMove(Move.ROCK)
      val expectedState = gameState.copy(round = 5, plays = List(Play(Move.PAPER, Some(Move.ROCK), Some(Result.WIN)), lastPlay))

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

  "setCurrentGuesstimater" should {
    "set currentGuesstimater value to 1" in {
      val gameState = GameState("opponent 1", 500, 1000, 100)
      when(mockCacheApi.get[GameState](meq(GameState.key))(any())).thenReturn(Some(gameState))

      serviceUnderTest.setCurrentGuesstimater(1)

      val newGameState = gameState.copy(currentGuesstimater = 1)
      verify(mockCacheApi, times(1)).set(meq(GameState.key), meq(newGameState), any())
    }

    "set currentGuesstimater value to 2" in {
      val gameState = GameState("opponent 1", 500, 1000, 100)
      when(mockCacheApi.get[GameState](meq(GameState.key))(any())).thenReturn(Some(gameState))

      serviceUnderTest.setCurrentGuesstimater(2)

      val newGameState = gameState.copy(currentGuesstimater = 2)
      verify(mockCacheApi, times(1)).set(meq(GameState.key), meq(newGameState), any())
    }
  }

  "setLastUpdateGuesstimater" should {
    "set lastUpdateGuesstimater value to 1" in {
      val gameState = GameState("opponent 1", 500, 1000, 100)
      when(mockCacheApi.get[GameState](meq(GameState.key))(any())).thenReturn(Some(gameState))

      serviceUnderTest.setLastUpdateGuesstimater(1)

      val newGameState = gameState.copy(lastUpdateGuesstimater = 1)
      verify(mockCacheApi, times(1)).set(meq(GameState.key), meq(newGameState), any())
    }

    "set lastUpdateGuesstimater value to 2" in {
      val gameState = GameState("opponent 1", 500, 1000, 100)
      when(mockCacheApi.get[GameState](meq(GameState.key))(any())).thenReturn(Some(gameState))

      serviceUnderTest.setLastUpdateGuesstimater(2)

      val newGameState = gameState.copy(lastUpdateGuesstimater = 2)
      verify(mockCacheApi, times(1)).set(meq(GameState.key), meq(newGameState), any())
    }
  }

  "getLastPlay" should {
    "return last play" when {
      "last play has result and opponent move" in {
        val expectedPlay = Play(Move.ROCK, Some(Move.PAPER), Some(Result.LOSE))
        val plays = List(expectedPlay)

        val gameState = GameState("opponent 1", 500, 1000, 100, 1, plays)
        when(mockCacheApi.get[GameState](meq(GameState.key))(any())).thenReturn(Some(gameState))

        val result = serviceUnderTest.getLastPlay()
        result shouldBe expectedPlay
      }
    }

    "return next to last play" when {
      "opponent move is None" in {
        val expectedPlay = Play(Move.ROCK, Some(Move.PAPER), Some(Result.LOSE))
        val plays = List(Play(Move.PAPER, None, Some(Result.WIN)), expectedPlay)

        val gameState = GameState("opponent 1", 500, 1000, 100, 1, plays)
        when(mockCacheApi.get[GameState](meq(GameState.key))(any())).thenReturn(Some(gameState))

        val result = serviceUnderTest.getLastPlay()
        result shouldBe expectedPlay
      }

      "result is None" in {
        val expectedPlay = Play(Move.ROCK, Some(Move.PAPER), Some(Result.LOSE))
        val plays = List(Play(Move.PAPER, Some(Move.PAPER)), expectedPlay)

        val gameState = GameState("opponent 1", 500, 1000, 100, 1, plays)
        when(mockCacheApi.get[GameState](meq(GameState.key))(any())).thenReturn(Some(gameState))

        val result = serviceUnderTest.getLastPlay()
        result shouldBe expectedPlay
      }
    }

    "throw exception" when {
      "no items in list" in {
        val gameState = GameState("opponent 1", 500, 1000, 100, 1, List())
        when(mockCacheApi.get[GameState](meq(GameState.key))(any())).thenReturn(Some(gameState))

        intercept[NoSuchElementException] {
          serviceUnderTest.getLastPlay()
        }
      }

      "opponent move is None" in {
        val gameState = GameState("opponent 1", 500, 1000, 100, 1, List(Play(Move.PAPER, None, Some(Result.WIN))))
        when(mockCacheApi.get[GameState](meq(GameState.key))(any())).thenReturn(Some(gameState))

        intercept[NoSuchElementException] {
          serviceUnderTest.getLastPlay()
        }
      }

      "result is None" in {
        val gameState = GameState("opponent 1", 500, 1000, 100, 1, List(Play(Move.PAPER, Some(Move.PAPER))))
        when(mockCacheApi.get[GameState](meq(GameState.key))(any())).thenReturn(Some(gameState))

        intercept[NoSuchElementException] {
          serviceUnderTest.getLastPlay()
        }
      }
    }
  }

  "getPlays" should {
    "return all plays" when {
      "1 play is in list" in {
        val plays = List(Play(Move.PAPER))

        val gameState = GameState("opponent 1", 500, 1000, 100, 1, plays)
        when(mockCacheApi.get[GameState](meq(GameState.key))(any())).thenReturn(Some(gameState))

        val result = serviceUnderTest.getPlays()

        result shouldBe plays
      }

      "2 play are in the list" in {
        val plays = List(Play(Move.PAPER), Play(Move.ROCK))

        val gameState = GameState("opponent 1", 500, 1000, 100, 1, plays)
        when(mockCacheApi.get[GameState](meq(GameState.key))(any())).thenReturn(Some(gameState))

        val result = serviceUnderTest.getPlays()

        result shouldBe plays
      }
    }
  }

  "hasDynamite" should {
    "return true when dynamite count is 1" in {
      val gameState = GameState("opponent 1", 500, 1000, 1)
      when(mockCacheApi.get[GameState](meq(GameState.key))(any())).thenReturn(Some(gameState))

      val result = serviceUnderTest.hasDynamite
      result shouldBe true
    }

    "return false when dynamite count is 0" in {
      val gameState = GameState("opponent 1", 500, 1000, 0)
      when(mockCacheApi.get[GameState](meq(GameState.key))(any())).thenReturn(Some(gameState))

      val result = serviceUnderTest.hasDynamite
      result shouldBe false
    }

    "return false when dynamite count is -1" in {
      val gameState = GameState("opponent 1", 500, 1000, -1)
      when(mockCacheApi.get[GameState](meq(GameState.key))(any())).thenReturn(Some(gameState))

      val result = serviceUnderTest.hasDynamite
      result shouldBe false
    }
  }
}