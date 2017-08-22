package guesstimater

import models.{Play, Result}

trait GameTheoryDefence {

  val playRandom: Random

  def predict(play: Play) = {
    if(play.result.contains(Result.DRAW)) playRandom.predict else play.opponentMove.get
  }
}