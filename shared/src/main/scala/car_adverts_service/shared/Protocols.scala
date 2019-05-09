package car_adverts_service.shared

import car_adverts_service.shared.models.{CarAdvert, Fuel}
import play.api.libs.json.{Json, OFormat, Reads}

/**
  * Created by DongHee Kim on 2019-05-09 오후 7:09.
  */

trait Protocols {
//  implicit val carAdvertListProtocolFormat = Json.format[CarAdvertListProtocol]
  implicit val carAdvertFormat = Json.format[CarAdvert]
  implicit val carAdvertReads = Json.reads[CarAdvert]
  //implicit val carAdvertListFormat = Json.reads[Seq[CarAdvert]]
  implicit val fuelFormat = Json.format[Fuel]
}
