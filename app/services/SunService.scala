package services

import java.time.format.DateTimeFormatter
import java.time.{ZoneId, ZonedDateTime}

import model.SunInfo
import play.api.libs.ws.{WSClient, WSResponse}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


class SunService(wsClient: WSClient) {
  def getSunInfo(lat: Double, lon: Double): Future[SunInfo] = {
    val sunResponseF: Future[WSResponse] = wsClient.url(SunService.getApiUrl(lat, lon)).get
    sunResponseF.map { sunResponse =>
      val sunJson = sunResponse.json
      val sunriseTimeStr: String = (sunJson \ "results" \ "sunrise").as[String]
      val sunsetTimeStr: String = (sunJson \ "results" \ "sunset").as[String]

      val sunriseTime = ZonedDateTime.parse(sunriseTimeStr)
      val sunsetTime = ZonedDateTime.parse(sunsetTimeStr)
      val formatter = DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.of("CET"))

      SunInfo(
        sunriseTime.format(formatter),
        sunsetTime.format(formatter)
      )
    }
  }
}

object SunService {
  def getApiUrl(lat: Double, lon: Double): String = "http://api.sunrise-sunset.org/json?" + f"lat=$lat&lng=$lon&formatted=0"
}
