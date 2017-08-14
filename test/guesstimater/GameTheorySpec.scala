package guesstimater

import models.{Move, Play, Result}
import org.scalatest.Matchers._
import org.scalatest.WordSpec

class GameTheorySpec extends WordSpec {
  def objectUnderTest = new GameTheory {}

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
  }
}