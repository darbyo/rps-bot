package services

import models.Result.Result
import models.{GameState, Move, Play, Result}
import org.mockito.Matchers.{eq=>meq}
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfterEach, WordSpec}

class GuesstimaterServiceSpec extends WordSpec with MockitoSugar with BeforeAndAfterEach {
  val mockGameStateService = mock[GameStateService]

  def serviceUnderTest = new CacheGuesstimaterService(mockGameStateService)

  def getGameState(current: Int, lastUpdated: Int, round: Int, plays: Seq[Play]): GameState =
    GameState("", 1000, 2000, 100,
      plays = plays,
      currentGuesstimater = current,
      lastUpdateGuesstimater = lastUpdated,
      round = round
    )

  def getPlay(result: Result): Play = Play(Move.ROCK, Some(Move.ROCK), Some(result))
  def getPlays(results: Result*): Seq[Play] = results.map(getPlay)
  def getPlays(n: Int, result: Result): Seq[Play] = (1 to n).map(_=>getPlay(result))
  def getWins(n: Int) = getPlays(n, Result.WIN)
  def getLosses(n: Int) = getPlays(n, Result.LOSE)

  override def beforeEach = {
    reset(mockGameStateService)
  }

  "updateCurrentGuesstimater" should {
    "increment currentGuesstimater to 1" when {
      "last 5 plays are losses" in {
        val gameState = getGameState(0, 1, 5, getLosses(5))
        when(mockGameStateService.getState()).thenReturn(gameState)

        serviceUnderTest.updateCurrentGuesstimater()

        verify(mockGameStateService, times(1)).setCurrentGuesstimater(meq(1))
      }

      "6 out of 10 last plays are losses" in {
        val plays = getLosses(6) ++ getWins(4)
        val gameState = getGameState(0, 1, 10, plays)
        when(mockGameStateService.getState()).thenReturn(gameState)

        serviceUnderTest.updateCurrentGuesstimater()

        verify(mockGameStateService, times(1)).setCurrentGuesstimater(meq(1))
      }

      "7 out of 14 last plays are losses" in {
        val plays = getLosses(7) ++ getWins(7)
        val gameState = getGameState(0, 1, 14, plays)
        when(mockGameStateService.getState()).thenReturn(gameState)

        serviceUnderTest.updateCurrentGuesstimater()

        verify(mockGameStateService, times(1)).setCurrentGuesstimater(meq(1))
      }
    }

    "increment currentGuesstimater to 2" when {
      "last 5 plays are losses" in {
        val gameState = getGameState(1, 1, 5, getLosses(5))
        when(mockGameStateService.getState()).thenReturn(gameState)

        serviceUnderTest.updateCurrentGuesstimater()

        verify(mockGameStateService, times(1)).setCurrentGuesstimater(meq(2))
      }

      "6 out of 10 last plays are losses" in {
        val plays = getLosses(6) ++ getWins(4)
        val gameState = getGameState(1, 1, 10, plays)
        when(mockGameStateService.getState()).thenReturn(gameState)

        serviceUnderTest.updateCurrentGuesstimater()

        verify(mockGameStateService, times(1)).setCurrentGuesstimater(meq(2))
      }

      "7 out of 14 last plays are losses" in {
        val plays = getLosses(7) ++ getWins(7)
        val gameState = getGameState(1, 1, 14, plays)
        when(mockGameStateService.getState()).thenReturn(gameState)

        serviceUnderTest.updateCurrentGuesstimater()

        verify(mockGameStateService, times(1)).setCurrentGuesstimater(meq(2))
      }
    }

    "set lastUpdateGuesstimater to round number" when {
      "last 5 plays are losses" in {
        val gameState = getGameState(1, 1, 5, getLosses(5))
        when(mockGameStateService.getState()).thenReturn(gameState)

        serviceUnderTest.updateCurrentGuesstimater()

        verify(mockGameStateService, times(1)).setLastUpdateGuesstimater(meq(5))
      }

      "6 out of 10 last plays are losses" in {
        val plays = getLosses(6) ++ getWins(4)
        val gameState = getGameState(1, 1, 10, plays)
        when(mockGameStateService.getState()).thenReturn(gameState)

        serviceUnderTest.updateCurrentGuesstimater()

        verify(mockGameStateService, times(1)).setLastUpdateGuesstimater(meq(10))
      }

      "7 out of 14 last plays are losses" in {
        val plays = getLosses(7) ++ getWins(7)
        val gameState = getGameState(1, 1, 14, plays)
        when(mockGameStateService.getState()).thenReturn(gameState)

        serviceUnderTest.updateCurrentGuesstimater()

        verify(mockGameStateService, times(1)).setLastUpdateGuesstimater(meq(14))
      }
    }

    "do not update currentGuesstimater" when {
      "1 out of 5 last plays are wins" in {
        val plays = getPlay(Result.WIN) +: getLosses(4)
        val gameState = getGameState(0, 1, 5, plays)
        when(mockGameStateService.getState()).thenReturn(gameState)

        serviceUnderTest.updateCurrentGuesstimater()

        verify(mockGameStateService, never).setCurrentGuesstimater(any())
      }

      "has 5 losses but not in a row" in {
        val plays = getLosses(2) ++ getWins(1) ++ getLosses(3)
        val gameState = getGameState(0, 1, 6, plays)
        when(mockGameStateService.getState()).thenReturn(gameState)

        serviceUnderTest.updateCurrentGuesstimater()

        verify(mockGameStateService, never).setCurrentGuesstimater(any())
      }

      "has 6 losses out 12 not in a row" in {
        val plays = getLosses(2) ++ getWins(6) ++ getLosses(4)
        val gameState = getGameState(0, 1, 12, plays)
        when(mockGameStateService.getState()).thenReturn(gameState)

        serviceUnderTest.updateCurrentGuesstimater()

        verify(mockGameStateService, never).setCurrentGuesstimater(any())
      }

      "has 7 losses out of 16 not in a row" in {
        val plays = getLosses(3) ++ getWins(9) ++ getLosses(4)
        val gameState = getGameState(0, 1, 16, plays)
        when(mockGameStateService.getState()).thenReturn(gameState)

        serviceUnderTest.updateCurrentGuesstimater()

        verify(mockGameStateService, never).setCurrentGuesstimater(any())
      }
    }

    "only check values since last round update" when {
      "5 loses in a row" in {
        val plays = getLosses(5)
        val gameState = getGameState(0, 2, 5, plays)
        when(mockGameStateService.getState()).thenReturn(gameState)

        serviceUnderTest.updateCurrentGuesstimater()

        verify(mockGameStateService, never).setCurrentGuesstimater(any())
      }

      "6 out of 10 losses" in {
        val plays = getLosses(6) ++ getWins(4)
        val gameState = getGameState(0, 2, 10, plays)
        when(mockGameStateService.getState()).thenReturn(gameState)

        serviceUnderTest.updateCurrentGuesstimater()

        verify(mockGameStateService, never).setCurrentGuesstimater(any())
      }

      "7 losses out of 14" in {
        val plays = getLosses(7) ++ getWins(7)
        val gameState = getGameState(0, 2, 14, plays)
        when(mockGameStateService.getState()).thenReturn(gameState)

        serviceUnderTest.updateCurrentGuesstimater()

        verify(mockGameStateService, never).setCurrentGuesstimater(any())
      }
    }
  }
}