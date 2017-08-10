package models

import play.api.libs.json.Reads

object Result extends Enumeration {
  type Result = Value

  val WIN, LOSE, DRAW = Value

  implicit val resultReads = Reads.enumNameReads(Result)
}