package guesstimater

import models.{Move, Play, Result}
import org.mockito.Mockito._
import org.scalatest.Matchers._
import org.scalatest.{BeforeAndAfterEach, WordSpec}
import org.scalatest.mockito.MockitoSugar

class GameTheorySpec extends WordSpec with MockitoSugar with BeforeAndAfterEach {
  val mockRandom = mock[Random]

  override def beforeEach = {
    reset(mockRandom)
  }

  def objectUnderTest = new GameTheory {
    val randomPick = mockRandom
  }

  "predict" should {
    "return rock" when {
      "previous play won with paper" in {
        val lastPlay = new Play(Move.PAPER, Some(Move.ROCK), Some(Result.WIN))
        val result = objectUnderTest.predict(lastPlay)

        result shouldBe Move.ROCK
      }

      "previous play lost against scissors" in {
        val lastPlay = new Play(Move.PAPER, Some(Move.SCISSORS), Some(Result.LOSE))
        val result = objectUnderTest.predict(lastPlay)

        result shouldBe Move.ROCK
      }
    }

    "return scissors" when {
      "previous play won with rock" in {
        val lastPlay = new Play(Move.ROCK, Some(Move.SCISSORS), Some(Result.WIN))
        val result = objectUnderTest.predict(lastPlay)

        result shouldBe Move.SCISSORS
      }

      "previous play lost against paper" in {
        val lastPlay = new Play(Move.ROCK, Some(Move.PAPER), Some(Result.LOSE))
        val result = objectUnderTest.predict(lastPlay)

        result shouldBe Move.SCISSORS
      }
    }

    "return paper" when {
      "previous play won with scissors" in {
        val lastPlay = new Play(Move.SCISSORS, Some(Move.PAPER), Some(Result.WIN))
        val result = objectUnderTest.predict(lastPlay)

        result shouldBe Move.PAPER
      }

      "previous play lost against rock" in {
        val lastPlay = new Play(Move.SCISSORS, Some(Move.ROCK), Some(Result.LOSE))
        val result = objectUnderTest.predict(lastPlay)

        result shouldBe Move.PAPER
      }
    }

    "return waterbomb" when {
      "previous play lost against dynamite" in {
        val lastPlay = new Play(Move.ROCK, Some(Move.DYNAMITE), Some(Result.LOSE))
        val result = objectUnderTest.predict(lastPlay)

        result shouldBe Move.WATERBOMB
      }
    }

    "return random" when {
      "draw with ROCK" in {
        when(mockRandom.predict).thenReturn(Move.ROCK)
        val  lastPlay = new Play(Move.SCISSORS, Some(Move.SCISSORS), Some(Result.DRAW))
        val result = objectUnderTest.predict(lastPlay)

        result shouldBe Move.ROCK
      }

      "draw with PAPER" in {
        when(mockRandom.predict).thenReturn(Move.SCISSORS)
        val lastPlay = new Play(Move.PAPER, Some(Move.PAPER), Some(Result.DRAW))
        val result = objectUnderTest.predict(lastPlay)

        result shouldBe Move.SCISSORS
      }
    }

    "return dynamite" when {
      "previous play won with waterbomb" in {
        val lastPlay = new Play(Move.WATERBOMB, Some(Move.DYNAMITE), Some(Result.WIN))
        val result = objectUnderTest.predict(lastPlay)

        result shouldBe Move.DYNAMITE
      }
    }
  }
}