package controllers

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import models.Move
import org.mockito.Matchers.{eq => meq}
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.BeforeAndAfterEach
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.libs.json.Json
import play.api.test.FakeRequest
import play.api.test.Helpers._
import services.{GameStateService, GuesstimaterService}

class MoveControllerSpec extends PlaySpec with GuiceOneAppPerTest with MockitoSugar with BeforeAndAfterEach {
  implicit val actorSystem = ActorSystem()
  implicit val actorMaterializer: akka.stream.Materializer = ActorMaterializer()

  val mockGameStateService = mock[GameStateService]
  val mockGuesstimaterService = mock[GuesstimaterService]
  def controller = new MoveController(mockGameStateService, mockGuesstimaterService)

  override def beforeEach = {
    reset(mockGameStateService)
    reset(mockGuesstimaterService)
  }

  "Move controller" must {
    "return 200 for a GET" in {
      when(mockGuesstimaterService.getGuess).thenReturn(Move.ROCK)

      val request = FakeRequest()
      val result = call(controller.move(), request)

      status(result) mustBe OK
    }

    """return "ROCK" as JSON for a GET""" in {
      when(mockGuesstimaterService.getGuess).thenReturn(Move.ROCK)

      val request = FakeRequest()
      val result = call(controller.move(), request)

      contentType(result) mustBe Some("application/json")
      contentAsString(result) mustBe """"ROCK""""
    }

    """return "PAPER" as JSON for a GET""" in {
      when(mockGuesstimaterService.getGuess).thenReturn(Move.PAPER)

      val request = FakeRequest()
      val result = call(controller.move(), request)

      contentType(result) mustBe Some("application/json")
      contentAsString(result) mustBe """"PAPER""""
    }
  }

  "lastOpponentMove" must {
    "return 200 for a POST" in {
      val json = """{"lastOpponentMove": "PAPER"}"""
      val request = FakeRequest().withJsonBody(Json.parse(json))
      val result = call(controller.lastOpponentMove(), request)

      status(result) mustBe OK
    }

    "add last opponent move to game state - test 1" in {
      val json = """{"lastOpponentMove": "PAPER"}"""
      val request = FakeRequest().withJsonBody(Json.parse(json))

      await(call(controller.lastOpponentMove(), request))

      verify(mockGameStateService, times(1)).addOpponentMove(meq(Move.PAPER))
    }

    "add last opponent move to game state - test 2" in {
      val json = """{"lastOpponentMove": "ROCK"}"""
      val request = FakeRequest().withJsonBody(Json.parse(json))

      await(call(controller.lastOpponentMove(), request))

      verify(mockGameStateService, times(1)).addOpponentMove(meq(Move.ROCK))
    }

    "return 400 when invalid json is passed" in {
      val json = """{"lstOpp": "PAPER"}"""
      val request = FakeRequest().withJsonBody(Json.parse(json))

      val result = call(controller.lastOpponentMove(), request)

      status(result) mustBe BAD_REQUEST
    }

    "return 400 when an invalid value is passed" in {
      val json = """{"lastOpponentMove":"PPER"}"""
      val request = FakeRequest().withJsonBody(Json.parse(json))

      val result = call(controller.lastOpponentMove(), request)

      status(result) mustBe BAD_REQUEST
    }

    "return 400 when game state service throws an exception" in {
      when(mockGameStateService.addOpponentMove(any())).thenThrow(new NoSuchElementException())

      val json = """{"lastOpponentMove": "PAPER"}"""
      val request = FakeRequest().withJsonBody(Json.parse(json))

      val result = call(controller.lastOpponentMove(), request)

      status(result) mustBe BAD_REQUEST
    }

    "call updateCurrentGuesstimater" in {
      val json = """{"lastOpponentMove": "PAPER"}"""
      val request = FakeRequest().withJsonBody(Json.parse(json))

      await(call(controller.lastOpponentMove(), request))

      verify(mockGuesstimaterService, times(1)).updateCurrentGuesstimater()
    }
  }
}
