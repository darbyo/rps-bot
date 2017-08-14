package models

import play.api.libs.json.Reads

object Move extends Enumeration {
  type Move = Value

  val ROCK, PAPER, SCISSORS, DYNAMITE, WATERBOMB = Value

  implicit val moveReads = Reads.enumNameReads(Move)

  def losesTo(ourMove: Move, opponentMove: Move) = {
    ourMove match {
      case Move.ROCK => in(opponentMove, Move.PAPER, Move.DYNAMITE)
      case Move.PAPER => in(opponentMove, Move.SCISSORS, Move.DYNAMITE)
      case Move.SCISSORS => in(opponentMove, Move.ROCK, Move.DYNAMITE)
      case Move.DYNAMITE => in(opponentMove, Move.WATERBOMB)
      case Move.WATERBOMB => in(opponentMove, Move.ROCK, Move.PAPER, Move.SCISSORS)
    }
  }

  def defeats(ourMove: Move, opponentMove: Move) = {
    ourMove match {
      case Move.ROCK => in(opponentMove, Move.SCISSORS, Move.WATERBOMB)
      case Move.PAPER => in(opponentMove, Move.ROCK, Move.WATERBOMB)
      case Move.SCISSORS => in(opponentMove, Move.PAPER, Move.WATERBOMB)
      case Move.DYNAMITE => in(opponentMove, Move.ROCK, Move.PAPER, Move.SCISSORS)
      case Move.WATERBOMB => in(opponentMove, Move.DYNAMITE)
    }
  }

  private def in(move: Move, moves: Move*) = moves.contains(move)
}