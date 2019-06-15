import org.scalatestplus.play._
import java.time.format.DateTimeFormatter
import java.time.{Instant, ZoneId, ZonedDateTime}

class ApplicationSpec extends PlaySpec {
  "DateTimeFormat" must {
    "return 1970 as the beginning of the epoch" in {
      val beginning = ZonedDateTime.ofInstant(Instant.ofEpochSecond(0), ZoneId.systemDefault())
      val formattedYear = beginning.format(DateTimeFormatter.ofPattern("YYYY"))
      formattedYear mustBe "1970"
    }
  }
}
