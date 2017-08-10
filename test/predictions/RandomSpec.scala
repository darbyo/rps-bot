package predictions

import org.scalatest.WordSpec
import org.scalatest.Matchers._
import org.mockito.Mockito._
import org.mockito.Matchers.{eq => meq}
import org.scalatest.mockito.MockitoSugar
import utils.RandomNumberGenerator

class RandomSpec extends WordSpec with MockitoSugar {
  val mockRandomNumberGenerator = mock[RandomNumberGenerator]

  def buildObject = new predictions.Random {
    val randomNumberGenerator = mockRandomNumberGenerator
  }

  "predict" should {
    "return rock" when {
      "random number generator returns 1" in {
        when(mockRandomNumberGenerator.Next(meq(1), meq(17))).thenReturn(1)
        buildObject.predict shouldBe "ROCK"
      }
      "random number generator returns 5" in {
        when(mockRandomNumberGenerator.Next(meq(1), meq(17))).thenReturn(5)
        buildObject.predict shouldBe "ROCK"
      }
    }

    "return paper" when {
      "random number generator return 6" in {
        when(mockRandomNumberGenerator.Next(meq(1), meq(17))).thenReturn(6)
        buildObject.predict shouldBe "PAPER"
      }
      "random number generator return 10" in {
        when(mockRandomNumberGenerator.Next(meq(1), meq(17))).thenReturn(10)
        buildObject.predict shouldBe "PAPER"
      }
    }

    "return scissors" when {
      "random number generator returns 11" in {
        when(mockRandomNumberGenerator.Next(meq(1), meq(17))).thenReturn(11)
        buildObject.predict shouldBe "SCISSORS"
      }
      "random number generator returns 15" in {
        when(mockRandomNumberGenerator.Next(meq(1), meq(17))).thenReturn(15)
        buildObject.predict shouldBe "SCISSORS"
      }
    }

    "return dynamite" when {
      "random number generator returns 16" in {
        when(mockRandomNumberGenerator.Next(meq(1), meq(17))).thenReturn(16)
        buildObject.predict shouldBe "DYNAMITE"
      }
    }
  }
}