package controllers

import javax.inject.Inject

import models.GameState
import org.joda.time.Instant
import play.api.mvc.{Action, Controller}
import services.GameStateService
import utils.Logs
import viewmodels.GameStateViewModel

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

class StartController @Inject()(gameStateService: GameStateService) extends Controller {
  def start() = Action.async(parse.json) { implicit request =>
    Try(request.body.as[GameStateViewModel]) match {
      case Success(m) => {
        val gameState = GameStateViewModel.asGameState(m)

        gameStateService.saveState(gameState)
        Logs.startGame(gameState)

        Future.successful(Ok)
      }
      case Failure(_) => Future.successful(BadRequest("Unable to parse request body"))
    }
  }

  def endGame() = Action { implicit request =>
    val gameState = gameStateService.getState()

    Logs.endGame(gameState)
    Logs.archive(archiveName(gameState))

    Ok("Game ended - logs archived")
  }

  private def archiveName(gameState: GameState) = s"${gameState.opponentName}-${Instant.now.getMillis}"
}
