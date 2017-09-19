package utils

import java.io.File
import java.nio.file.{Files, StandardCopyOption}

import guesstimater.Guesstimater
import models.GameState
import play.api.Logger

object Logs {
  def startGame(gameState: GameState) =
    Logger.info(
      s"""
         |Begin new game:
         |  Opponent: ${gameState.opponentName}
         |  MaxRounds: ${gameState.maxRounds}
         |  PointsToWin: ${gameState.pointsToWin}
         |  DynamiteCount: ${gameState.dynamiteCount}
          """.stripMargin)

  def endGame(gameState: GameState) =
    Logger.info(
      s"""
         |Game end:
         |  $gameState
      """.stripMargin)

  def guesstimaterUpdated(gameState: GameState, oldGuesstimater: Guesstimater, newGuesstimater: Guesstimater) =
    Logger.info(
      s"""
         |Round ${gameState.round}:
         |  Old guesstimater: $oldGuesstimater
         |  New guesstimater: $newGuesstimater
       """.stripMargin)

  def archive(archiveName: String) = {
    val src = new File("./logs/application.log")
    val dest = new File(s"./logs/$archiveName.log")

    Files.copy(src.toPath, dest.toPath, StandardCopyOption.REPLACE_EXISTING)
    Files.delete(src.toPath)
  }

  def list = new File("./logs").list
}