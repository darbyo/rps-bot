package guesstimater

import models.{Move, Play, Result}
import org.scalatest.Matchers._
import org.scalatest.WordSpec

class GameTheoryDefenceSpec extends WordSpec {
  def objectUnderTest = new GameTheoryDefence {}

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
  }
}