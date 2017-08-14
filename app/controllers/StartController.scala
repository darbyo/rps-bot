package controllers

import javax.inject.Inject

import models.GameState
import play.api.mvc.{Action, Controller}
import services.GameStateService
import viewmodels.GameStateViewModel

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

class StartController @Inject()(gameStateService: GameStateService) extends Controller {
  def start() = Action.async(parse.json) { implicit request =>
    Try(request.body.as[GameStateViewModel]) match {
      case Success(m) => {
        gameStateService.saveState(GameStateViewModel.asGameState(m))
        Future.successful(Ok)
      }
      case Failure(_) => Future.successful(BadRequest("Unable to parse request body"))
    }
  }
}
