package filters

import akka.actor.ActorSystem
import akka.stream.Materializer
import play.api.mvc.{Filter, RequestHeader, Result}
import play.api.Logger

import scala.concurrent.Future

class StatsFilter(actorSystem: ActorSystem, implicit val mat: Materializer) extends Filter {
    private val log = Logger(this.getClass)
  override def apply(nextFilter: RequestHeader => Future[Result])(header: RequestHeader): Future[Result] = {
    log.info(s"Serving another request: ${header.path}")
    nextFilter(header)
  }
}
