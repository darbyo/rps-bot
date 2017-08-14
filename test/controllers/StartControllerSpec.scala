package controllers

import akka.actor.ActorSystem
import akka.stream._
import models.GameState
import org.mockito.Mockito._
import org.mockito.Matchers.{eq => meq}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.libs.json.Json
import play.api.test.FakeRequest
import play.api.test.Helpers._
import services.GameStateService

class StartControllerSpec extends PlaySpec with GuiceOneAppPerTest with MockitoSugar with BeforeAndAfterEach {
  implicit val actorSystem = ActorSystem()
  implicit val actorMaterializer: akka.stream.Materializer = ActorMaterializer()

  val mockGameStateService = mock[GameStateService]
  def controller = new StartController(mockGameStateService)

  override def beforeEach = {
    reset(mockGameStateService)
  }

  "Start controller" must {
    "return 200 for a POST" in {
      val json =
        """{
          | "opponentName": "ABC123",
          | "pointsToWin": 1000,
          | "maxRounds": 2000,
          | "dynamiteCount": 100
          | }""".stripMargin

      val request = FakeRequest().withJsonBody(Json.parse(json))
      val result = call(controller.start(), request)

      status(result) mustBe OK
    }

    "save game state - test 1" in {
      val json =
        """{
          | "opponentName": "opponent 1",
          | "pointsToWin": 1000,
          | "maxRounds": 2000,
          | "dynamiteCount": 100
          | }""".stripMargin

      val request = FakeRequest().withJsonBody(Json.parse(json))
      await(call(controller.start(), request))

      verify(mockGameStateService, times(1)).saveState(meq(GameState("opponent 1", 1000, 2000, 100)))
    }

    "save game state - test 2" in {
      val json =
        """{
          | "opponentName": "ABC123",
          | "pointsToWin": 500,
          | "maxRounds": 1000,
          | "dynamiteCount": 20
          | }""".stripMargin

      val request = FakeRequest().withJsonBody(Json.parse(json))
      await(call(controller.start(), request))

      verify(mockGameStateService, times(1)).saveState(meq(GameState("ABC123", 500, 1000, 20)))
    }

    "return 400 when json is invalid" in {
      val json =
        """{
          | "pointsToWin": 1000,
          | "maxRounds": 2000,
          | "dynamiteCount": 100
          | }""".stripMargin

      val request = FakeRequest().withJsonBody(Json.parse(json))
      val result = call(controller.start(), request)

      status(result) mustBe BAD_REQUEST
    }
  }
}
