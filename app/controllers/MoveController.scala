package controllers

import javax.inject.Inject

import models.Move
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import services.{GameStateService, GuesstimaterService}
import viewmodels.OpponentMove

import scala.concurrent.Future
import scala.util.{Failure, Random, Success, Try}

class MoveController @Inject() (
  gameStateService: GameStateService,
  guesstimaterService: GuesstimaterService
) extends Controller {

  def getRandom = Random.shuffle(List(Move.ROCK, Move.PAPER, Move.SCISSORS)).head

  def move() = Action {
    val ourMove = guesstimaterService.getGuess
    val o = if(ourMove == Move.DYNAMITE && gameStateService.getState().dynamiteCount == 0) {
      getRandom
    } else {
      ourMove
    }
    gameStateService.addOurMove(o)
    Ok(Json.toJson(o))
  }

  def lastOpponentMove() = Action.async(parse.json) { implicit request =>
    Try(request.body.as[OpponentMove]) match {
      case Success(m) => {
        try {
          gameStateService.addOpponentMove(m.lastOpponentMove)
          guesstimaterService.updateCurrentGuesstimater()

          Future.successful(Ok)
        } catch {
          case _: NoSuchElementException => Future.successful(BadRequest("Invalid game state"))
        }
      }
      case Failure(_) => Future.successful(BadRequest("Unable to parse request body"))
    }
  }
}
