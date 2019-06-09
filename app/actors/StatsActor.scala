package actors

import actors.StatsActor.{GetStats, Ping, RequestReceived}
import akka.actor.Actor
import play.api.Logger

class StatsActor extends Actor {
  private val log = Logger(this.getClass)
  var counter = 0

  override def receive: Receive = {
    case Ping => log.info("Ground control to major Tom")
    case RequestReceived => counter += 1
    case GetStats => sender() ! counter
  }
}

object StatsActor {
  val name = "statsActor"
  val path = s"/user/$name"

  case object Ping
  case object RequestReceived
  case object GetStats
}
