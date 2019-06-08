import controllers.Application
import play.api.ApplicationLoader.Context
import play.api._
import play.api.libs.ws.ahc.AhcWSComponents
import play.api.mvc._
import router.Routes
import play.api.routing.Router
import com.softwaremill.macwire._
import _root_.controllers.AssetsComponents
import play.api
import play.filters.HttpFiltersComponents
import services.{SunService, WeatherService}

import scala.concurrent.Future

class AppApplicationLoader extends ApplicationLoader {
  def load(context: Context): api.Application = {
    LoggerConfigurator(context.environment.classLoader).foreach { cfg =>
      cfg.configure(context.environment)
    }
    new AppComponents(context).application
  }
}

class AppComponents(context: Context) extends BuiltInComponentsFromContext(context) with AhcWSComponents
  with AssetsComponents with HttpFiltersComponents {

  override lazy val controllerComponents: DefaultControllerComponents = wire[DefaultControllerComponents]
  lazy val prefix: String = "/"
  lazy val router: Router = wire[Routes]
  lazy val applicationController: Application = wire[Application]

  lazy val sunService: SunService = wire[SunService]
  lazy val weatherService: WeatherService = wire[WeatherService]

  val onStart: Unit = {
    Logger.info("Starting the app...")
  }

  applicationLifecycle.addStopHook { () =>
    Logger.info("Stopping the app...")
    Future.successful(Unit)
  }

}
