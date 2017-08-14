package models

import models.Move.Move
import models.Move.{defeats, losesTo}
import play.api.libs.json.Reads

object Result extends Enumeration {
  type Result = Value

  val WIN, LOSE, DRAW = Value

  implicit val resultReads = Reads.enumNameReads(Result)

  def fromPlay(ourMove: Move, opponentMove: Move): Result = {
    if (defeats(ourMove, opponentMove)) Result.WIN
    else if (losesTo(ourMove, opponentMove)) Result.LOSE
    else Result.DRAW
  }
}



