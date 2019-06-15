import controllers.Application
import play.api.ApplicationLoader.Context
import play.api._
import play.api.libs.ws.ahc.AhcWSComponents
import play.api.mvc._
import router.Routes
import play.api.routing.Router
import com.softwaremill.macwire._
import _root_.controllers.AssetsComponents
import actors.StatsActor
import actors.StatsActor.Ping
import akka.actor.{ActorRef, Props}
import filters.StatsFilter
import play.api
import play.api.cache.caffeine.CaffeineCacheComponents
import play.api.db.{DBComponents, HikariCPComponents}
import play.api.db.evolutions.{DynamicEvolutions, EvolutionsComponents}
import play.filters.HttpFiltersComponents
import services.{AuthService, SunService, WeatherService}
import scalikejdbc.config.DBs

import scala.concurrent.Future

class AppApplicationLoader extends ApplicationLoader {
  def load(context: Context): api.Application = {
    LoggerConfigurator(context.environment.classLoader).foreach { cfg =>
      cfg.configure(context.environment)
    }
    new AppComponents(context).application
  }
}

class AppComponents(context: Context)
  extends BuiltInComponentsFromContext(context)
    with AhcWSComponents with EvolutionsComponents
    with DBComponents with HikariCPComponents
    with CaffeineCacheComponents with AssetsComponents with HttpFiltersComponents {

  private val log = Logger(this.getClass)

  override lazy val controllerComponents: DefaultControllerComponents = wire[DefaultControllerComponents]
  lazy val prefix: String = "/"
  lazy val router: Router = wire[Routes]

  lazy val statsFilter: Filter = wire[StatsFilter]
  override lazy val httpFilters: Seq[Filter] = Seq(statsFilter)

  lazy val applicationController: Application = wire[Application]
  lazy val sunService: SunService = wire[SunService]
  lazy val weatherService: WeatherService = wire[WeatherService]
  lazy val authService = new AuthService(defaultCacheApi.sync)

  lazy val statsActor: ActorRef = actorSystem.actorOf(Props(wire[StatsActor]), StatsActor.name)

  override lazy val dynamicEvolutions: DynamicEvolutions = new DynamicEvolutions

  val onStart: Unit = {
    log.info("Starting the app...")
    applicationEvolutions
    DBs.setupAll()
    statsActor ! Ping
  }

  applicationLifecycle.addStopHook { () =>
    log.info("Stopping the app...")
    DBs.closeAll()
    Future.successful(Unit)
  }

}
