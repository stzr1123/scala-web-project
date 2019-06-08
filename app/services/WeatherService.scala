package services

import play.api.libs.ws.WSClient
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class WeatherService(wsClient: WSClient) {
  def getTemperature(lat: Double, long: Double): Future[Double] = {
    val weatherResponseF = wsClient.url(WeatherService.getApiUrl(lat, long)).get
    weatherResponseF.map { weatherResponse =>
      val weatherJson = weatherResponse.json
      val temperature = (weatherJson \ "main" \ "temp").as[Double]
      temperature
    }
  }
}

object WeatherService {
  val apiKey: String = "89265e73d53a913dc87cf4ac87faf499"
  val apiUrl: String = f"http://api.openweathermap.org/data/2.5/weather?APPID=$apiKey"
  def formatLatLon(lat: Double, long: Double): String = f"&units=metric&lat=$lat&lon=$long"

  def getApiUrl(lat: Double, long: Double): String = apiUrl + formatLatLon(lat, long)
}
