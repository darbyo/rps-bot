package guesstimater

import models.Move.Move

trait Guesstimater {
  val name: String
  def getGuess: Move

  override def toString: String = name
}