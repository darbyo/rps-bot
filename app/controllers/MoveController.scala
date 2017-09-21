package controllers

import javax.inject.Inject

import models.Move
import play.api.Logger
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

    Logger.info(s"Our Move: $nextMove")

    Ok(Json.toJson(nextMove))
  }

  def lastOpponentMove() = Action.async(parse.json) { implicit request =>
    Logger.info(request.body.toString)

    Try(request.body.as[OpponentMove]) match {
      case Success(m) => {
        try {
          Logger.info(s"Opp Move: ${m.opponentLastMove}")

          gameStateService.addOpponentMove(m.opponentLastMove)
          guesstimaterService.updateCurrentGuesstimater()

          Future.successful(Ok)
        } catch {
          case _: NoSuchElementException => {
            Logger.debug("lastOpponentMove: Invalid game state")

            Future.successful(BadRequest("Invalid game state"))
          }
        }
      }
      case Failure(_) => {
        Logger.debug("lastOpponentMove: Unable to parse request body")

        Future.successful(BadRequest("Unable to parse request body"))
      }
    }
  }
}
