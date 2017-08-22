package controllers

import javax.inject.Inject

import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import services.{GameStateService, GuesstimaterService}
import viewmodels.OpponentMove

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

class MoveController @Inject() (
  gameStateService: GameStateService,
  guesstimaterService: GuesstimaterService
) extends Controller {

  def move() = Action {
    Ok(Json.toJson(guesstimaterService.getGuess))
  }

  def lastOpponentMove() = Action.async(parse.json) { implicit request =>
    Try(request.body.as[OpponentMove]) match {
      case Success(m) => {
        try {
          gameStateService.addOpponentMove(m.lastOpponentMove)
          guesstimaterService.updateCurrentGuesstimater()
          Future.successful(Ok)
        }catch {
          case _: NoSuchElementException => Future.successful(BadRequest("Invalid game state"))
        }
      }
      case Failure(_) => Future.successful(BadRequest("Unable to parse request body"))
    }
  }
}
