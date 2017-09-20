package services

import com.google.inject.{ImplementedBy, Inject}
import guesstimater._

@ImplementedBy(classOf[CGuesstimaters])
trait Guesstimaters {
  def get: List[Guesstimater]
}

class CGuesstimaters @Inject() (
  gameTheory: GameTheory,
  gameTheoryDefence: GameTheoryDefence,
  opponentMostCommon: OpponentMostCommon,
  opponentLast50MostCommon: OpponentLast50MostCommon,
  opponentLast10MostCommon: OpponentLast10MostCommon,
  ourMostCommon: OurMostCommon,
  ourLast50MostCommon: OurLast50MostCommon,
  ourLast10MostCommon: OurLast10MostCommon,
  random: Random
) extends Guesstimaters {
  def get = List(
    random,
    opponentMostCommon,
    gameTheory,
    random,
    opponentLast10MostCommon,
    gameTheoryDefence,
    random,
    opponentLast50MostCommon,
  )
}