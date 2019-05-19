package car_adverts_service.shared

import car_adverts_service.shared.models.{CarAdvert, CarAdvertToUpdate, Fuel}
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json.{JsPath, JsValue, Json, OFormat}

/**
  * Created by DongHee Kim on 2019-05-09 오후 7:09.
  */

case class JsonResult(isSuccess: Boolean, message: Option[JsValue] = None)

case class CarAdvertLimitedList(list: Seq[CarAdvert], offset: Option[String] = None)
case class CarAdvertLimitedList2(list: Seq[CarAdvert], offset: Option[(String, String)] = None)

case class FilteredByPrice(price:Int, offset: Option[(String, String)] = None)

trait Protocols {
  private val carAdvertWrites = Json.writes[CarAdvert]
  private val carAdvertReads = (
    (JsPath \ "id").read[String](minLength[String](1)) and
      (JsPath \ "title").read[String](minLength[String](1)) and
      (JsPath \ "fuel").read[String](minLength[String](1)) and
      (JsPath \ "price").read[Int](min[Int](0)) and
      (JsPath \ "newThing").read[Boolean] and
      (JsPath \ "mileage").readNullable[Int](min(0)) and
      (JsPath \ "firstRegistration").readNullable[String] and
      (JsPath \ "sort").read[Int]
    ) (CarAdvert.apply _)
  //implicit val car1AdvertFormat = Json.format[CarAdvert]
  implicit val carAdvertFormat = OFormat(carAdvertReads, carAdvertWrites)

  implicit val carAdvertUpdateFormat = Json.format[CarAdvertToUpdate]
  implicit val fuelFormat = Json.format[Fuel]
  implicit val jsonResultFormat = Json.format[JsonResult]
  implicit val carAdvertLimitedListFormat = Json.format[CarAdvertLimitedList]
  implicit val carAdvertLimitedList2Format = Json.format[CarAdvertLimitedList2]
  implicit val filteredByPriceFormat = Json.format[FilteredByPrice]
}
