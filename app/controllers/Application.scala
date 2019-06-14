package controllers

import java.util.concurrent.TimeUnit

import actors.StatsActor
import akka.actor.ActorSystem
import akka.util.Timeout
import akka.pattern.ask
import controllers.Assets.Asset
import model.CombinedData
import play.api.mvc._
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global
import services.{SunService, WeatherService}


class Application(components: ControllerComponents,
                  assets: Assets,
                  sunService: SunService,
                  weatherService: WeatherService,
                  actorSystem: ActorSystem) extends AbstractController(components) {

  def index: Action[AnyContent] = Action {
    Ok(views.html.index())
  }

  def login: Action[AnyContent] = Action {
    Ok(views.html.login())
  }

  def data: Action[AnyContent] = Action.async {

    val lat: Double = 48.1222839
    val lon: Double = 11.5402938

    val sunInfoF = sunService.getSunInfo(lat, lon)
    val weatherInfoF = weatherService.getTemperature(lat, lon)

    implicit val timeout: Timeout = Timeout(5, TimeUnit.SECONDS)
    val requestsF = (actorSystem.actorSelection(StatsActor.path) ? StatsActor.GetStats).mapTo[Int]

    for {
      sunInfo <- sunInfoF
      temperature <- weatherInfoF
      requests <- requestsF
    } yield {
      Ok(Json.toJson(CombinedData(sunInfo, temperature, requests)))
    }
  }

  def versioned(path: String, file: Asset): Action[AnyContent] = assets.versioned(path, file)
}
