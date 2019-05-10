package controllers

import car_adverts_service.shared.models.{CarAdvert, CarAdvertToUpdate, Fuel}
import car_adverts_service.shared.{JsonResult, Protocols}
import dbs.repositories.Fuels
import javax.inject.{Inject, Singleton}
import org.scanamo.error.ScanamoError
import play.api.Logging
import play.api.libs.json.Reads._
import play.api.libs.json.{JsError, JsPath, Json, JsonValidationError}
import play.api.mvc.{MessagesAbstractController, MessagesControllerComponents}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by DongHee Kim on 2019-05-11 오전 12:57.
  */
@Singleton
class FuelController @Inject()(fuels: Fuels,
                               mcc: MessagesControllerComponents) extends MessagesAbstractController(mcc) with Logging with Protocols {
  implicit val ec = ExecutionContext.global

  val fuelNameReads = ((JsPath \ "name").read[String](minLength[String](2)))

  def index = Action { implicit request =>
    Ok(views.html.pages.fuels())
  }

  def list = Action.async {
    fuels.getList.map { list =>
      Ok(Json.toJson(list))
    }
  }

  def insert = Action(parse.json).async { implicit request =>
    val result = request.body.validate[String](fuelNameReads).fold(
      errors => Future.successful(BadRequest(JsError.toJson(errors))),
      fuel =>
        fuels.insert(Fuel(fuel)).map {
          case Some(fuel) => BadRequest(Json.toJson(JsonResult(false)))
          case None => Ok(Json.toJson(JsonResult(true)))
        }
    )
    result
  }

  def delete(id:String) = Action(parse.json).async { implicit request =>
    val result = request.body.validate[String](fuelNameReads).fold(
      errors => Future.successful(BadRequest(JsError.toJson(errors))),
      fuel =>
        fuels.isExist(fuel).flatMap {
          case true =>
            fuels.delete(fuel).map { deleteItemResult =>
              Ok(Json.toJson(JsonResult(true)))
            }
          case false => Future.successful(BadRequest(Json.toJson(JsonResult(false))))
        }
    )
    result
  }
}