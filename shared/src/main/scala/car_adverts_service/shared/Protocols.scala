package car_adverts_service.shared

import car_adverts_service.shared.models.{CarAdvert, CarAdvertToUpdate, Fuel}
import play.api.libs.json.{Format, JsError, JsPath, JsValue, Json, JsonValidationError, OFormat, Reads}
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._

/**
  * Created by DongHee Kim on 2019-05-09 오후 7:09.
  */

case class JsonResult(isSuccess: Boolean, message: Option[JsValue] = None)

trait Protocols {
  private val carAdvertWrites = Json.writes[CarAdvert]
  private val carAdvertReads = (
    (JsPath \ "id").read[String](minLength[String](1)) and
    (JsPath \ "title").read[String](minLength[String](1)) and
      (JsPath \ "fuel").read[String](minLength[String](1)) and
      (JsPath \ "price").read[Int](min[Int](0)) and
      (JsPath \ "newThing").read[Boolean] and
      (JsPath \ "mileage").readNullable[Int](min(0)) and
      (JsPath \ "firstRegistration").readNullable[String]
    )(CarAdvert.apply _)
  //implicit val car1AdvertFormat = Json.format[CarAdvert]
  implicit val carAdvertFormat = OFormat(carAdvertReads, carAdvertWrites)

  implicit val carAdvertUpdateFormat = Json.format[CarAdvertToUpdate]
  implicit val fuelFormat = Json.format[Fuel]
  implicit val jsonResultFormat = Json.format[JsonResult]
}
