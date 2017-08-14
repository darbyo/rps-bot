package guesstimater

import models.Move
import utils.RandomNumberGenerator


trait Random {
  val randomNumberGenerator: RandomNumberGenerator

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
}