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


class Application @Inject() (components: ControllerComponents, assets: Assets, ws: WSClient)
    extends AbstractController(components) {
  def index: Action[AnyContent] = Action.async {
    val date: Date = new Date()
    val dateStr: String = new SimpleDateFormat().format(date)

    val responseF = ws.url("http://api.sunrise-sunset.org/json?" + "lat=48.1222839&lng=11.5402938&formatted=0").get()
    responseF.map { response =>
      val json = response.json
      val sunriseTimeStr = (json \ "results" \ "sunrise").as[String]
      val sunsetTimeStr = (json \ "results" \ "sunset").as[String]

      val sunriseTime = ZonedDateTime.parse(sunriseTimeStr)
      val sunsetTime = ZonedDateTime.parse(sunsetTimeStr)
      val formatter = DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.of("CET"))

      val sunInfo = SunInfo(
        sunriseTime.format(formatter),
        sunsetTime.format(formatter)
      )

      Ok(views.html.index(sunInfo))
    }
  }

  def versioned(path: String, file: Asset): Action[AnyContent] = assets.versioned(path, file)
}
