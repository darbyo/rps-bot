package controllers

import com.google.inject.Inject
import play.api.mvc.{Action, Controller}
import services.GameStateService

class StartController @Inject()(gameStateService: GameStateService) extends Controller {
  def start() = Action {
    Ok
  }
}
