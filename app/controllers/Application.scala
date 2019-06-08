package controllers

import controllers.Assets.Asset
import javax.inject._
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import model.SunInfo
import play.api.libs.ws._
import java.time.{ZoneId, ZonedDateTime}
import java.time.format.DateTimeFormatter

import play.api.libs.json.JsValue
import services.{SunService, WeatherService}

import scala.concurrent.Future


class Application @Inject() (components: ControllerComponents, assets: Assets, ws: WSClient)
    extends AbstractController(components) {

  val sunService = new SunService(ws)
  val weatherService = new WeatherService(ws)

  def index: Action[AnyContent] = Action.async {

    val lat: Double = 48.1222839
    val lon: Double = 11.5402938

    val sunInfoF = sunService.getSunInfo(lat, lon)
    val weatherInfoF = weatherService.getTemperature(lat, lon)

    for {
      sunInfo <- sunInfoF
      temperature <- weatherInfoF
    } yield {
      Ok(views.html.index(sunInfo, temperature))
    }
  }

  def versioned(path: String, file: Asset): Action[AnyContent] = assets.versioned(path, file)
}
