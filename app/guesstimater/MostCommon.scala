package guesstimater

import com.google.inject.{ImplementedBy, Inject}
import models.Move
import models.Move.Move
import services.GameStateService

trait MostCommon extends Guesstimater {
  def predict(items: List[Move]) = {
    val papers = items.count(_ == Move.PAPER)
    val rocks = items.count(_ == Move.ROCK)
    val scissors = items.count(_ == Move.SCISSORS)
    val dynamite = items.count(_ == Move.DYNAMITE)

    if (dynamite > scissors && dynamite > papers && dynamite > rocks) {
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


@ImplementedBy(classOf[COpponentMostCommon])
trait OpponentMostCommon extends MostCommon

class COpponentMostCommon @Inject() (gameState: GameStateService) extends OpponentMostCommon {
  def getGuess = predict(gameState.getPlays().filter(_.opponentMove.isDefined).map(_.opponentMove.get))
}


@ImplementedBy(classOf[COpponentLast50MostCommon])
trait OpponentLast50MostCommon extends MostCommon

class COpponentLast50MostCommon @Inject() (gameState: GameStateService) extends OpponentLast50MostCommon {
  def getGuess = predict(gameState.getPlays().filter(_.opponentMove.isDefined).take(50).map(_.opponentMove.get))
}


@ImplementedBy(classOf[COpponentLast10MostCommon])
trait OpponentLast10MostCommon extends MostCommon

class COpponentLast10MostCommon @Inject() (gameState: GameStateService) extends OpponentLast10MostCommon {
  def getGuess = predict(gameState.getPlays().filter(_.opponentMove.isDefined).take(10).map(_.opponentMove.get))
}


@ImplementedBy(classOf[COurMostCommon])
trait OurMostCommon extends MostCommon

class COurMostCommon @Inject() (gameState: GameStateService) extends OurMostCommon {
  def getGuess = predict(gameState.getPlays().map(_.ourMove))
}


@ImplementedBy(classOf[COurLast50MostCommon])
trait OurLast50MostCommon extends MostCommon

class COurLast50MostCommon @Inject() (gameState: GameStateService) extends OurLast50MostCommon {
  def getGuess = predict(gameState.getPlays().take(50).map(_.ourMove))
}


@ImplementedBy(classOf[COurLast10MostCommon])
trait OurLast10MostCommon extends MostCommon

class COurLast10MostCommon @Inject() (gameState: GameStateService) extends OurLast10MostCommon {
  def getGuess = predict(gameState.getPlays().take(10).map(_.ourMove))
}