package guesstimater

import models.Move.Move

trait Guesstimater {
  def getGuess: Move
}