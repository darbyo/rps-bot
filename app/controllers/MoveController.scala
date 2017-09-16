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

  private def getRandom = Random.shuffle(List(Move.ROCK, Move.PAPER, Move.SCISSORS)).head
  private def getNextMove = guesstimaterService.getGuess match {
    case Move.DYNAMITE if !gameStateService.hasDynamite => getRandom
    case x => x
  }

  def move() = Action {
    val nextMove = getNextMove
    gameStateService.addOurMove(nextMove)
    Ok(Json.toJson(nextMove))
  }

  def lastOpponentMove() = Action.async(parse.json) { implicit request =>
    Try(request.body.as[OpponentMove]) match {
      case Success(m) => {
        try {
          gameStateService.addOpponentMove(m.lastOpponentMove)
          guesstimaterService.updateCurrentGuesstimater()

          gameStateService.logIfGameEnd()

          Future.successful(Ok)
        } catch {
          case _: NoSuchElementException => Future.successful(BadRequest("Invalid game state"))
        }
      }
      case Failure(_) => Future.successful(BadRequest("Unable to parse request body"))
    }
  }
}
