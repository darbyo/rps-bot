package guesstimater

import com.google.inject.ImplementedBy
import models.Move
import utils.RandomNumberGenerator

@ImplementedBy(classOf[CRandom])
trait Random extends Guesstimater

class CRandom extends Random {
  val name = "Weighted random"
  val randomNumberGenerator: RandomNumberGenerator = RandomNumberGenerator

  def predict = {
    val random = randomNumberGenerator.Next(1, 17)

    if(random <= 5) {
      Move.ROCK
    } else if (random <= 10) {
      Move.PAPER
    } else if (random <= 15) {
      Move.SCISSORS
    } else {
      Move.DYNAMITE
    }
  }

  def getGuess = predict
}