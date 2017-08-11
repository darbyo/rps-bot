package predictions

import models.{Move, Play, Result}

trait GameTheory {
  def predict(play: Play) = {
    if(play.result == Some(Result.LOSE)) {
      if(play.opponentMove == Some(Move.PAPER)){
        Move.SCISSORS
      } else if(play.opponentMove == Some(Move.ROCK)) {
        Move.PAPER
      } else if (play.opponentMove == Some(Move.DYNAMITE)) {
        Move.WATERBOMB
      } else Move.ROCK
  } else {
      if(play.ourMove == (Move.PAPER)) {
        Move.ROCK
      }
      else
      Move.SCISSORS
   }
  }
}