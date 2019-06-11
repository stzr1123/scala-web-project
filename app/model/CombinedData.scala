package model

import play.api.libs.json.{Json, OWrites}

case class CombinedData(sunInfo: SunInfo, temperature: Double, requests: Int)


object CombinedData {
  implicit val writes: OWrites[CombinedData] = Json.writes[CombinedData]
}

