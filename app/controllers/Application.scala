package controllers

import controllers.Assets.Asset
import javax.inject._
import play.api.mvc._
import java.util.Date
import java.text.SimpleDateFormat

import scala.concurrent.ExecutionContext.Implicits.global
import model.SunInfo
import play.api.libs.ws._
import java.time.{ZoneId, ZonedDateTime}
import java.time.format.DateTimeFormatter

import play.api.libs.json.{JsLookupResult, JsValue}

import scala.concurrent.Future


class Application @Inject() (components: ControllerComponents, assets: Assets, ws: WSClient)
    extends AbstractController(components) {
  def index: Action[AnyContent] = Action.async {

    val lat: String = "48.1222839"
    val long: String = "11.5402938"
    val apiKey: String = "89265e73d53a913dc87cf4ac87faf499"

    val responseF: Future[WSResponse] =
      ws.url("http://api.sunrise-sunset.org/json?" + f"lat=$lat&lng=$long&formatted=0").get

    val responseWeatherF: Future[WSResponse] =
      ws.url("http://api.openweathermap.org/data/2.5/weather?" + f"units=metric&lat=$lat&lon=$long&APPID=$apiKey").get

    for {
      responseAstro <- responseF
      responseWeather <- responseWeatherF
    } yield {
      val weatherJson: JsValue = responseWeather.json
      val temperature: Double = (weatherJson \ "main" \ "temp").as[Double]

      val json: JsValue = responseAstro.json
      val sunriseTimeStr: String = (json \ "results" \ "sunrise").as[String]
      val sunsetTimeStr: String = (json \ "results" \ "sunset").as[String]

      val sunriseTime = ZonedDateTime.parse(sunriseTimeStr)
      val sunsetTime = ZonedDateTime.parse(sunsetTimeStr)
      val formatter = DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.of("CET"))

      val sunInfo = SunInfo(
        sunriseTime.format(formatter),
        sunsetTime.format(formatter)
      )

      Ok(views.html.index(sunInfo, temperature))
    }
  }

  def versioned(path: String, file: Asset): Action[AnyContent] = assets.versioned(path, file)
}
