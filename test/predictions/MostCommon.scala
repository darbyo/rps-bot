package predictions

import org.scalatest.WordSpec
import org.scalatest.Matchers._

class MostCommonSpec extends WordSpec {
  "predict method" should {
    "return scissors" when {
      "paper is only item in list" in {
        val result = MostCommon.predict(List("PAPER"))
        result shouldBe "SCISSORS"
      }

      "paper is second in the list" in {
        val result = MostCommon.predict(List("ROCK", "PAPER", "PAPER"))
        result shouldBe "SCISSORS"
      }

      "paper is the first 2 items in the list" in {
        val result = MostCommon.predict(List("PAPER", "PAPER", "ROCK"))
        result shouldBe "SCISSORS"
      }

      "paper appears thrice in the list" in {
        val result = MostCommon.predict(List("ROCK", "ROCK", "PAPER", "PAPER", "PAPER"))
        result shouldBe "SCISSORS"
      }

      "paper appears thrice in the list, rock appears twice and scissors appears twice" in {
        val result = MostCommon.predict(List("PAPER", "ROCK", "ROCK", "PAPER", "SCISSORS", "PAPER", "SCISSORS"))
        result shouldBe "SCISSORS"
      }
    }
  }

  "return paper" when {
    "rock is only item in list" in {
      val result = MostCommon.predict(List("ROCK"))
      result shouldBe "PAPER"
    }

    "rock appears twice in list" in {
      val result = MostCommon.predict(List("ROCK", "ROCK", "PAPER"))
      result shouldBe "PAPER"
    }

    "rock appears thrice in the list" in {
      val result = MostCommon.predict(List("ROCK", "ROCK", "ROCK", "SCISSORS"))
      result shouldBe "PAPER"
    }

    "rock appears thrice in the list and paper twice and scissors twice" in {
      val result = MostCommon.predict(List("ROCK", "ROCK", "ROCK", "PAPER", "PAPER", "SCISSORS", "SCISSORS"))
      result shouldBe "PAPER"
    }

    "rock appears twice in the list and dynamite once" in {
      val result = MostCommon.predict(List("ROCK", "ROCK", "DYNAMITE"))
      result shouldBe "PAPER"
    }
  }

  "return rock" when {
    "scissors are the only item in list" in {
      val result = MostCommon.predict(List("SCISSORS"))
      result shouldBe "ROCK"
    }

    "scissors appears twice in the list" in {
      val result = MostCommon.predict(List("SCISSORS", "SCISSORS", "ROCK"))
      result shouldBe "ROCK"
    }

    "scissors appears thrice in the list" in {
      val result = MostCommon.predict(List("SCISSORS", "SCISSORS", "SCISSORS", "ROCK"))
      result shouldBe "ROCK"
    }

    "scissors appears thrice in the list and rock appears twice" in {
      val result = MostCommon.predict(List("SCISSORS", "SCISSORS", "SCISSORS", "ROCK", "ROCK"))
      result shouldBe "ROCK"
    }

    "scissors appears thrice in the list, paper appears twice and rock appears twice" in {
      val result = MostCommon.predict(List("ROCK", "PAPER", "PAPER", "SCISSORS", "SCISSORS", "ROCK", "SCISSORS"))
      result shouldBe "ROCK"
    }

    "dynamite appears once in the list and scissors appears twice" in {
      val result = MostCommon.predict(List("DYNAMITE", "SCISSORS", "SCISSORS"))
      result shouldBe "ROCK"
    }
  }

  "return waterbomb" when {
    "dynamite appears by itself in the list" in {
      val result = MostCommon.predict(List("DYNAMITE"))
      result shouldBe "WATERBOMB"
    }

    "dynamite appears twice in the list" in {
      val result = MostCommon.predict(List("DYNAMITE", "DYNAMITE", "ROCK"))
      result shouldBe "WATERBOMB"
    }

    "dynamite appears twice in the list and paper appears once" in {
      val result = MostCommon.predict(List("DYNAMITE", "DYNAMITE", "PAPER"))
      result shouldBe "WATERBOMB"
    }
  }

  "return dynamite" when {
    "the list is empty" in {
      val result = MostCommon.predict(List())
      result shouldBe "DYNAMITE"
    }

    "rock paper and scissors are equal" in {
      val result = MostCommon.predict(List("ROCK", "ROCK", "PAPER", "PAPER", "SCISSORS", "SCISSORS"))
      result shouldBe "DYNAMITE"
    }

    "rocks and papers are equal and greater scissors" in {
      val result = MostCommon.predict(List("ROCK", "ROCK", "PAPER", "PAPER", "SCISSORS"))
      result shouldBe "DYNAMITE"
    }
  }
}

