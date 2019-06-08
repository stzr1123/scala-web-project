package controllers

import controllers.Assets.Asset
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global

import services.{SunService, WeatherService}


class Application(components: ControllerComponents, assets: Assets,
                  sunService: SunService, weatherService: WeatherService)
    extends AbstractController(components) {

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
