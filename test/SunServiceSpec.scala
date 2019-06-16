import org.scalatestplus.play._
import org.mockito.Mockito._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import play.api.libs.ws.{WSClient, WSRequest, WSResponse}
import play.api.libs.json.{JsValue, Json}
import services.SunService

import scala.concurrent.Future


class SunServiceSpec extends PlaySpec with MockitoSugar with ScalaFutures {
  "SunService" must {
    "retrieve correct sunset and sunrise information" in {
      val wsClientStub = mock[WSClient]
      val wsRequestStub = mock[WSRequest]
      val wsResponseStub = mock[WSResponse]

      when(wsResponseStub.json).thenReturn(FixturesSunS.jsApiResult)
      when(wsRequestStub.get).thenReturn(Future.successful(wsResponseStub))
      when(wsClientStub.url(FixturesSunS.url)).thenReturn(wsRequestStub)

      val sunService = new SunService(wsClientStub)
      val resultF = sunService.getSunInfo(FixturesSunS.lat, FixturesSunS.lon)

      whenReady(resultF) { res =>
        res.sunrise mustBe "07:01:12"
        res.sunset mustBe "23:15:23"
      }
    }
  }

}

object FixturesSunS {
  val lat: Double = 48.1222839
  val lon: Double = 11.5402938

  val url: String = SunService.getApiUrl(lat, lon)

  val expectedApiResponse: String =
    """{
        "results": {
          "sunrise": "2019-06-16T05:01:12+00:00",
          "sunset": "2019-06-16T21:15:23+00:00"
        }
    }""".stripMargin

  val jsApiResult: JsValue = Json.parse(expectedApiResponse)
}
