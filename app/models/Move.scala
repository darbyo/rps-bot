package models

import play.api.libs.json.Reads

object Move extends Enumeration {
  type Move = Value

  val ROCK, PAPER, SCISSORS, DYNAMITE, WATERBOMB = Value

  implicit val moveReads = Reads.enumNameReads(Move)
}