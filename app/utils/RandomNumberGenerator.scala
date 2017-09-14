package utils

import scala.util.Random

object RandomNumberGenerator extends RandomNumberGenerator {
  def Next(from: Int, to: Int): Int = {
    require(from < to)

    val diff = to - from
    Random.nextInt(diff + 1) + from
  }
}

trait RandomNumberGenerator {
  def Next(from: Int, to: Int): Int
}