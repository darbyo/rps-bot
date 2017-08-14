package guesstimater

import models.Move
import models.Move.Move

object MostCommon {
  def predict(items: List[Move]) = {
    val papers = items.count(_ == Move.PAPER)
    val rocks = items.count(_ == Move.ROCK)
    val scissors = items.count(_ == Move.SCISSORS)
    val dynamite = items.count(_ == Move.DYNAMITE)

    if (dynamite > scissors && dynamite > papers && dynamite > rocks){
      Move.WATERBOMB
    } else if (papers > rocks && papers > scissors) {
      Move.SCISSORS
    } else if(scissors > rocks && scissors > papers) {
      Move.ROCK
    } else if (rocks > papers && rocks > scissors) {
      Move.PAPER
    } else {
      Move.DYNAMITE
    }
  }
}
