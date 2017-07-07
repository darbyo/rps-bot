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
        val result = MostCommon.predict(List("ROCK","PAPER","PAPER"))
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
    }
  }
}
