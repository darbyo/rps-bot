package guesstimater

import models.Move
import org.scalatest.WordSpec
import org.scalatest.Matchers._

class MostCommonSpec extends WordSpec {
  def objectUnderTest = new MostCommon {}

  "predict method" should {
    "return scissors" when {
      "paper is only item in list" in {
        val result = objectUnderTest.predict(List(Move.PAPER))
        result shouldBe Move.SCISSORS
      }

      "paper is second in the list" in {
        val result = objectUnderTest.predict(List(Move.ROCK, Move.PAPER, Move.PAPER))
        result shouldBe Move.SCISSORS
      }

      "paper is the first 2 items in the list" in {
        val result = objectUnderTest.predict(List(Move.PAPER, Move.PAPER, Move.ROCK))
        result shouldBe Move.SCISSORS
      }

      "paper appears thrice in the list" in {
        val result = objectUnderTest.predict(List(Move.ROCK, Move.ROCK, Move.PAPER, Move.PAPER, Move.PAPER))
        result shouldBe Move.SCISSORS
      }

      "paper appears thrice in the list, rock appears twice and scissors appears twice" in {
        val result = objectUnderTest.predict(List(Move.PAPER, Move.ROCK, Move.ROCK, Move.PAPER, Move.SCISSORS, Move.PAPER, Move.SCISSORS))
        result shouldBe Move.SCISSORS
      }
    }
  }

  "return paper" when {
    "rock is only item in list" in {
      val result = objectUnderTest.predict(List(Move.ROCK))
      result shouldBe Move.PAPER
    }

    "rock appears twice in list" in {
      val result = objectUnderTest.predict(List(Move.ROCK, Move.ROCK, Move.PAPER))
      result shouldBe Move.PAPER
    }

    "rock appears thrice in the list" in {
      val result = objectUnderTest.predict(List(Move.ROCK, Move.ROCK, Move.ROCK, Move.SCISSORS))
      result shouldBe Move.PAPER
    }

    "rock appears thrice in the list and paper twice and scissors twice" in {
      val result = objectUnderTest.predict(List(Move.ROCK, Move.ROCK, Move.ROCK, Move.PAPER, Move.PAPER, Move.SCISSORS, Move.SCISSORS))
      result shouldBe Move.PAPER
    }

    "rock appears twice in the list and dynamite once" in {
      val result = objectUnderTest.predict(List(Move.ROCK, Move.ROCK, Move.DYNAMITE))
      result shouldBe Move.PAPER
    }
  }

  "return rock" when {
    "scissors are the only item in list" in {
      val result = objectUnderTest.predict(List(Move.SCISSORS))
      result shouldBe Move.ROCK
    }

    "scissors appears twice in the list" in {
      val result = objectUnderTest.predict(List(Move.SCISSORS, Move.SCISSORS, Move.ROCK))
      result shouldBe Move.ROCK
    }

    "scissors appears thrice in the list" in {
      val result = objectUnderTest.predict(List(Move.SCISSORS, Move.SCISSORS, Move.SCISSORS, Move.ROCK))
      result shouldBe Move.ROCK
    }

    "scissors appears thrice in the list and rock appears twice" in {
      val result = objectUnderTest.predict(List(Move.SCISSORS, Move.SCISSORS, Move.SCISSORS, Move.ROCK, Move.ROCK))
      result shouldBe Move.ROCK
    }

    "scissors appears thrice in the list, paper appears twice and rock appears twice" in {
      val result = objectUnderTest.predict(List(Move.ROCK, Move.PAPER, Move.PAPER, Move.SCISSORS, Move.SCISSORS, Move.ROCK, Move.SCISSORS))
      result shouldBe Move.ROCK
    }

    "dynamite appears once in the list and scissors appears twice" in {
      val result = objectUnderTest.predict(List(Move.DYNAMITE, Move.SCISSORS, Move.SCISSORS))
      result shouldBe Move.ROCK
    }
  }

  "return waterbomb" when {
    "dynamite appears by itself in the list" in {
      val result = objectUnderTest.predict(List(Move.DYNAMITE))
      result shouldBe Move.WATERBOMB
    }

    "dynamite appears twice in the list" in {
      val result = objectUnderTest.predict(List(Move.DYNAMITE, Move.DYNAMITE, Move.ROCK))
      result shouldBe Move.WATERBOMB
    }

    "dynamite appears twice in the list and paper appears once" in {
      val result = objectUnderTest.predict(List(Move.DYNAMITE, Move.DYNAMITE, Move.PAPER))
      result shouldBe Move.WATERBOMB
    }
  }

  "return dynamite" when {
    "the list is empty" in {
      val result = objectUnderTest.predict(List())
      result shouldBe Move.DYNAMITE
    }

    "rock paper and scissors are equal" in {
      val result = objectUnderTest.predict(List(Move.ROCK, Move.ROCK, Move.PAPER, Move.PAPER, Move.SCISSORS, Move.SCISSORS))
      result shouldBe Move.DYNAMITE
    }

    "rocks and papers are equal and greater scissors" in {
      val result = objectUnderTest.predict(List(Move.ROCK, Move.ROCK, Move.PAPER, Move.PAPER, Move.SCISSORS))
      result shouldBe Move.DYNAMITE
    }
  }
}

