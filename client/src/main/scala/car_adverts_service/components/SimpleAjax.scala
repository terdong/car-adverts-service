package car_adverts_service.components

import car_adverts_service.shared.Protocols
import car_adverts_service.shared.models.CarAdvert
import org.scalajs.dom.ext.Ajax
import org.scalajs.dom.raw.XMLHttpRequest
import play.api.libs.json.{JsArray, JsError, JsSuccess, JsValue, Json, OFormat, Reads}
import org.scalajs.dom._

import scala.concurrent.ExecutionContext.Implicits.global
/**
  * Created by DongHee Kim on 2019-05-09 오후 6:15.
  */
trait SimpleAjax{

  lazy val headers = Map("Content-Type" -> "application/json", "Csrf-Token" -> "sksms3ehrd6lfrkrh9tlvek")
  lazy val defaultHeaders = Map("Csrf-Token" -> "sksms3ehrd6lfrkrh9tlvek")

  def resultByCommon[Protocol](xhr: XMLHttpRequest)(implicit jsonFormat : OFormat[Protocol]) = {
    Json.parse(xhr.responseText).validate[Protocol] match {
      case s:JsSuccess[Protocol] => s.asOpt
      case e:JsError => {
        console.error(s"An invalid data has been received.\nerror: ${e.toString}")
        e.asOpt
      }
    }
  }

  def resultWithList[Protocol](xhr: XMLHttpRequest)(implicit jsonFormat : OFormat[Protocol]) = {
    Json.parse(xhr.responseText).validate[Seq[Protocol]] match {
      case s:JsSuccess[Seq[Protocol]] => s.asOpt
      case e:JsError => {
        console.error(s"An invalid data has been received.\nerror: ${e.toString}")
        e.asOpt
      }
    }
  }

  def resultByJaValue(xhr:XMLHttpRequest) = {
    Json.parse(xhr.responseText)
  }

  def post[Protocol](url:String, data:Protocol)(implicit jsonFormat : OFormat[Protocol]) = {
    Ajax.post(
      url = url,
      data = Json.prettyPrint(Json.toJson(data)),
      headers = headers
    ).map(resultByCommon(_))
  }

  def getList[Protocol](url:String)(implicit jsonFormat : OFormat[Protocol]) = {
    Ajax.get(
      url = url,
      headers = defaultHeaders
    ).map(resultWithList(_))
  }
  def get[Protocol](url:String)(implicit jsonFormat : OFormat[Protocol]) = {
    Ajax.get(
      url = url,
      headers = defaultHeaders
    ).map(resultByCommon(_))
  }
/*  def get(url:String) = {
    Ajax.get(
      url = url,
      headers = defaultHeaders
    ).map(resultByJaValue(_))
  }*/
}
