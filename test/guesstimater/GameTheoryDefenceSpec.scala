package guesstimater

import models.{Move, Play, Result}
import org.mockito.Mockito.{reset, when}
import org.scalatest.Matchers._
import org.scalatest.{BeforeAndAfterEach, WordSpec}
import org.scalatest.mockito.MockitoSugar
import services.GameStateService

class GameTheoryDefenceSpec extends WordSpec with MockitoSugar with BeforeAndAfterEach {
  val mockGameStateService = mock[GameStateService]
  val mockRandom = mock[Random]

  override def beforeEach = {
    reset(mockGameStateService)
    reset(mockRandom)
  }

  def objectUnderTest = new CGameTheoryDefence(mockRandom, mockGameStateService)

  "predict" should {
    "return rock" when {
      "previous play lost and opponent move was rock" in {
        val previousPlay = new Play(Move.SCISSORS, Some(Move.ROCK), Some(Result.LOSE))
        val result = objectUnderTest.predict(previousPlay)

        result shouldBe Move.ROCK
      }
    }

    "return scissors" when {
      "previous play lost and opponent move was scissors" in {
        val previousPlay = new Play(Move.PAPER, Some(Move.SCISSORS), Some(Result.LOSE))
        val result = objectUnderTest.predict(previousPlay)

        result shouldBe Move.SCISSORS
      }
    }

    "return paper" when {
      "previous play lost and opponent move was paper" in {
        val previousPlay = new Play(Move.ROCK, Some(Move.PAPER), Some(Result.LOSE))
        val result = objectUnderTest.predict(previousPlay)

        result shouldBe Move.PAPER
      }
    }

    "return dynamite" when {
      "previous play lost and opponent move was dynamite" in {
        val previousPlay = new Play(Move.ROCK, Some(Move.DYNAMITE), Some(Result.LOSE))
        val result = objectUnderTest.predict(previousPlay)

        result shouldBe Move.DYNAMITE
      }
    }

    "return waterbomb" when {
      "previous play lost and opponent move was waterbomb" in {
        val previousPlay = new Play(Move.DYNAMITE, Some(Move.WATERBOMB), Some(Result.LOSE))
        val result = objectUnderTest.predict(previousPlay)

        result shouldBe Move.WATERBOMB
      }
    }

    "return random" when {
      "previous play was a draw - ROCK" in {
        when(mockRandom.getGuess).thenReturn(Move.ROCK)
        val previousPlay = new Play(Move.PAPER, Some(Move.PAPER), Some(Result.DRAW))
        val result = objectUnderTest.predict(previousPlay)

        result shouldBe Move.ROCK
      }

      "previous play was a draw - PAPER" in {
        when(mockRandom.getGuess).thenReturn(Move.PAPER)
        val previousPlay = new Play(Move.PAPER, Some(Move.PAPER), Some(Result.DRAW))
        val result = objectUnderTest.predict(previousPlay)

        result shouldBe Move.PAPER
      }

      "previous play was a draw - SCISSORS" in {
        when(mockRandom.getGuess).thenReturn(Move.SCISSORS)
        val previousPlay = new Play(Move.PAPER, Some(Move.PAPER), Some(Result.DRAW))
        val result = objectUnderTest.predict(previousPlay)

        result shouldBe Move.SCISSORS
      }

      "previous play was a draw - DYNAMITE" in {
        when(mockRandom.getGuess).thenReturn(Move.DYNAMITE)
        val previousPlay = new Play(Move.PAPER, Some(Move.PAPER), Some(Result.DRAW))
        val result = objectUnderTest.predict(previousPlay)

        result shouldBe Move.DYNAMITE
      }
    }
  }
}