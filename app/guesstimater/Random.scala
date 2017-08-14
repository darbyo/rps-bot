package guesstimater

import utils.RandomNumberGenerator


trait Random {
  val randomNumberGenerator: RandomNumberGenerator

  def predict = {
    val random = randomNumberGenerator.Next(1, 17)

    if(random <= 5) {
      "ROCK"
    } else if (random <= 10) {
      "PAPER"
    } else if (random <= 15) {
      "SCISSORS"
    } else {
      "DYNAMITE"
    }
  }
}