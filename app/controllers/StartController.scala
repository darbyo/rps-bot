package controllers

import javax.inject.Inject

import play.api.Logger
import play.api.mvc.{Action, Controller}
import services.GameStateService
import viewmodels.GameStateViewModel

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

class StartController @Inject()(gameStateService: GameStateService) extends Controller {
  def start() = Action.async(parse.json) { implicit request =>
    Try(request.body.as[GameStateViewModel]) match {
      case Success(m) => {
        Logger.logger.info(
          s"""
             |Begin new game:
             |  Opponent: ${m.opponentName}
             |  MaxRounds: ${m.maxRounds}
             |  PointsToWin: ${m.pointsToWin}
             |  DynamiteCount: ${m.dynamiteCount}
          """.stripMargin)

        gameStateService.saveState(GameStateViewModel.asGameState(m))
        Future.successful(Ok)
      }
      case Failure(_) => Future.successful(BadRequest("Unable to parse request body"))
    }
  }
}
