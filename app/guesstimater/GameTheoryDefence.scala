package guesstimater

import models.Play

trait GameTheoryDefence {
  def predict(play: Play) = {
    play.opponentMove.get
  }
}