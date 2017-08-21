package guesstimater

import models.{Move, Play, Result}

trait GameTheory {
  val randomPick: Random

  def predict(play: Play) = {
    if(play.result == Some(Result.LOSE)) {
      if(play.opponentMove == Some(Move.PAPER)){
        Move.SCISSORS
      } else if(play.opponentMove == Some(Move.ROCK)) {
        Move.PAPER
      } else if (play.opponentMove == Some(Move.DYNAMITE)) {
        Move.WATERBOMB
      } else Move.ROCK
  } else if(play.result == Some(Result.WIN)) {
      if(play.ourMove == (Move.PAPER)) {
        Move.ROCK
      }else if(play.ourMove == Move.SCISSORS) {Move.PAPER}
      else if (play.ourMove == Move.WATERBOMB){
        Move.DYNAMITE
      }
      else {
      Move.SCISSORS
      }
   }
    else randomPick.predict
  }
}