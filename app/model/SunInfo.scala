package model

import play.api.libs.json.{Json, OWrites}

case class SunInfo(sunrise: String, sunset: String)

object SunInfo {
  implicit val writes: OWrites[SunInfo] = Json.writes[SunInfo]
}
