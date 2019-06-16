import play.api.ApplicationLoader.Context
import play.api.libs.ws.ahc.AhcWSComponents
import play.api.{BuiltInComponentsFromContext, NoHttpFiltersComponents}
import com.softwaremill.macwire._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Seconds, Span}
import org.scalatestplus.play.PlaySpec
import play.api.routing.Router
import services.WeatherService
import org.scalatestplus.play.components.OneAppPerSuiteWithComponents

class TestAppComponents(context: Context) extends BuiltInComponentsFromContext(context)
  with NoHttpFiltersComponents with AhcWSComponents {

  lazy val router: Router = Router.empty
  lazy val weatherService: WeatherService = wire[WeatherService]
}

class WeatherServiceSpec extends PlaySpec with OneAppPerSuiteWithComponents with ScalaFutures {

  override def components = new TestAppComponents(context)
  override implicit val patienceConfig: PatienceConfig = PatienceConfig(timeout = Span(5, Seconds))

  "WeatherService" must {
    "return a meaningful temperature" in {
      val resultF = components.weatherService.getTemperature(FixturesWeatherS.lat, FixturesWeatherS.lon)
      whenReady(resultF) { result =>
        result mustBe >=(-25.0)
        result mustBe <=(40.0)
      }

    }
  }
}

object FixturesWeatherS {
  val lat: Double = 48.1222839
  val lon: Double = 11.5402938
}
