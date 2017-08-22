package guesstimater

import models.{Move, Play, Result}

trait GameTheory {
  val randomPick: Random

  def predict(play: Play) = {
    if(play.result.contains(Result.LOSE)) {
      if(play.opponentMove.contains(Move.PAPER)) Move.SCISSORS
      else if(play.opponentMove.contains(Move.ROCK)) Move.PAPER
      else if(play.opponentMove.contains(Move.DYNAMITE)) Move.WATERBOMB
      else Move.ROCK
    } else if(play.result.contains(Result.WIN)) {
      if(play.ourMove == Move.PAPER) Move.ROCK
      else if(play.ourMove == Move.SCISSORS) Move.PAPER
      else if(play.ourMove == Move.WATERBOMB) Move.DYNAMITE
      else Move.SCISSORS
    }
    else randomPick.predict
  }
}