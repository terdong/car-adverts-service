package controllers

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date

import car_adverts_service.shared.models.{CarAdvert, CarAdvertToUpdate}
import car_adverts_service.shared.{JsonResult, Protocols}
import com.fasterxml.uuid.Generators
import dbs.repositories.{CarAdverts, Fuels}
import javax.inject._
import org.scanamo.error.ScanamoError
import play.api.Logging
import play.api.cache.Cached
import play.api.data.Forms._
import play.api.data._
import play.api.data.validation.Constraints._
import play.api.libs.json.{JsError, JsPath, Json, JsonValidationError}
import play.api.mvc._
import play.api.routing.JavaScriptReverseRouter

import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Random, Try}

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()(carAdverts: CarAdverts,
                               fuels: Fuels,
                               cached: Cached,
                               mcc: MessagesControllerComponents) extends MessagesAbstractController(mcc) with Logging with Protocols {
  implicit val ec = ExecutionContext.global

  val carAdvertForm = Form(
    mapping(
      "id" -> text,
      "title" -> nonEmptyText,
      "fuel" -> nonEmptyText,
      "price" -> number.verifying(min(0)),
      "newThing" -> boolean,
      "mileage" -> optional(number.verifying(min(0))),
      "firstRegistration" -> optional(nonEmptyText)
    )(CarAdvert.apply)(CarAdvert.unapply).verifying("You must input fields!", carAdvert =>
      !(!carAdvert.newThing && (carAdvert.mileage.isEmpty || carAdvert.firstRegistration.isEmpty))
    )
  )

  import play.api.libs.functional.syntax._
  import play.api.libs.json.Reads._

  val format: SimpleDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd")
  val dateReads = verifying[String] { str =>
    val result = Try(format.parse(str)).isSuccess
    result
  }

/*  val carAdvertReadsForServer = (
    (JsPath \ "id").read[String](minLength[String](1)) and
      (JsPath \ "title").read[String](minLength[String](1)) and
      (JsPath \ "fuel").read[String](minLength[String](1)) and
      (JsPath \ "price").read[Int](min[Int](0)) and
      (JsPath \ "newThing").read[Boolean] and
      (JsPath \ "mileage").readNullable[Int](min[Int](0)) and
      (JsPath \ "firstRegistration").readNullable[String](dateReads.map(str => format.format(format.parse(str))))
    ) (CarAdvert.apply _)*/

  val carAdvertUpdateReadsForServer = (
    (JsPath \ "title").readNullable[String](minLength[String](1)) and
      (JsPath \ "fuel").readNullable[String](minLength[String](1)) and
      (JsPath \ "price").readNullable[Int](min[Int](0)) and
      (JsPath \ "newThing").readNullable[Boolean] and
      (JsPath \ "mileage").readNullable[Int](min[Int](0)) and
      (JsPath \ "firstRegistration").readNullable[String](dateReads.map(str => format.format(format.parse(str))))
    ) (CarAdvertToUpdate.apply _)

  /**
    * Create an Action to render an HTML page with a welcome message.
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def index = Action.async { implicit request =>
    //    Ok(views.html.pages.list(List.empty[CarAdvert]))
    carAdverts.getMaxSize.map(maxSize => Ok(views.html.pages.list(maxSize)))
  }

  def createForm = Action.async { implicit request =>
    fuels.getList.map{ list =>
      Ok(views.html.pages.create_form(carAdvertForm, list))
    }
  }

  def edit(id: String) = Action(parse.json).async { implicit request =>
    val result = request.body.validate[CarAdvertToUpdate](carAdvertUpdateReadsForServer).fold(
      (errors: Seq[(JsPath, Seq[JsonValidationError])]) =>
        Future.successful(BadRequest(JsError.toJson(errors))),
      carAdvertUpdate =>
        carAdverts.update(id, carAdvertUpdate).map {
          case Some(Right(c: CarAdvert)) => Ok(Json.toJson(JsonResult(true, Some(Json.toJson(c)))))
          case None => logger.warn("Nothing to change."); Ok(Json.toJson(JsonResult(false)))
          case Some(Left(error: ScanamoError)) =>
            logger.error(error.toString)
            carAdverts.delete(id)
            InternalServerError(Json.toJson(JsonResult(false)))
        }
    )
    result
  }

  def create = Action.async { implicit request =>
    val result = carAdvertForm.bindFromRequest.fold(
      formWithErrors => {
        fuels.getList.map{ list =>
          BadRequest(views.html.pages.create_form(formWithErrors, list))
        }
      },
      carAdvertData => {
        carAdverts.insert(carAdvertData).map {
          case Some(c) => Ok(c.toString)
          case None => Redirect(routes.HomeController.index)
        }
      }
    )
    result
  }

  def delete(id: String) = Action.async {
    carAdverts.isExist(id).flatMap {
      case true =>
        carAdverts.delete(id).map { deleteItemResult =>
          Ok(Json.toJson(JsonResult(true)))
        }
      case false => Future.successful(BadRequest(Json.toJson(JsonResult(false))))
    }
  }

  def list = Action.async { implicit request =>
    carAdverts.getList.map { list =>
      val json = Json.toJson(list)
      Ok(json)
    }
  }

  def search(id: String) = Action.async {
    if (id.isEmpty) {
      Future.successful(BadRequest(Json.toJson(JsonResult(false, Some(Json.toJson(routes.HomeController.index().url))))))
    } else {
      carAdverts.findById(id).map {
        case Some(carAdvert) =>
          Ok(Json.toJson(JsonResult(true, Some(Json.toJson(carAdvert)))))
        case None =>
          BadRequest(Json.toJson(JsonResult(false)))
      }
    }
  }

  def listFilteredByPrice(price: Int) = Action.async { implicit request =>
    if (price < 0) {
      Future.successful(BadRequest(Json.toJson(JsonResult(false, Some(Json.toJson(routes.HomeController.index().url))))))
    } else {
      carAdverts.getListFilterByPrice(price).map { list =>
        val json = Json.toJson(list)
        Ok(json)
      }
    }
  }

  def listSortedByField(field: String) = Action.async {

    val r = field match {
      case "title" => carAdverts.getListSortByField(_.title)
      case "fuel" => carAdverts.getListSortByField(_.fuel)
      case "price" => carAdverts.getListSortByField(_.price)
      case "newThing" => carAdverts.getListSortByField(_.newThing)
      case "mileage" => carAdverts.getListSortByField(_.mileage)
      case "firstRegistration" => carAdverts.getListSortByField(_.firstRegistration)
    }

    r.map { list =>
      val json = Json.toJson(list)
      Ok(json)
    }
  }

  /*  def insertFuel = Action.async{

    }*/

  def generateRandomCarAdverts100 = Action.async {
    val set = mutable.Set.empty[CarAdvert]

    val start = LocalDate.of(2019, 1, 1)
    val end = LocalDate.of(2019, 5, 31)

    import scala.language.postfixOps
    for (i <- 1 to 100) {
      val uuid = Generators.timeBasedGenerator().generate()
      val title = Random.alphanumeric.filter(_.isLetterOrDigit).take(Random.nextInt(10) + 1).mkString
      val fuel = if (Random.nextBoolean()) "gasoline" else "diesel"
      val price = Random.nextInt(1000) * 1000
      if (Random.nextBoolean()) {
        val date = LocalDate.ofEpochDay(start.toEpochDay + Random.nextInt((end.toEpochDay - start.toEpochDay) toInt))
        set += CarAdvert(uuid.toString, title, fuel, price, false, Some(Random.nextInt(999999) + 1), Some(date.toString))
      } else {
        set += CarAdvert(uuid.toString, title, fuel, price, true)
      }
    }

    carAdverts.insertAll(set.toSet).map { _ =>
      Redirect("/")
    }
  }

  def insertTest = Action.async {
    //val uuid = TimeBasedUUID(java.util.UUID).toString
    val uuid = Generators.timeBasedGenerator().generate()
    val sdf = new SimpleDateFormat("yyyy-MM-dd")
    val date = sdf.format(new Date())
    //    carAdverts.insert(CarAdvert(uuid , "Audi A4 Avant", "gasoline",1000, true, None, None )).map{
    carAdverts.insert(CarAdvert(uuid.toString, "Audi A4 Avant", "gasoline", 1000)).map {
      case Some(c: CarAdvert) => Ok(s"already exists = ${c.toString}")
      case None => Ok("cool")
    }
  }

  def updateTest = Action.async {
    val id = "abcd2"
    val carAdvertToUpdate = CarAdvertToUpdate(title = Some("Tico"))
    carAdverts.isExist(id).flatMap {
      case true => carAdverts.update(id, carAdvertToUpdate).map {
        //case false => InternalServerError("Not found target by the key.")
        case Some(Right(c: CarAdvert)) => Ok(s"updated = ${c.toString}.")
        case None => Ok("Nothing to change.")
        case Some(Left(error: ScanamoError)) =>
          logger.error(error.toString)
          carAdverts.delete(id)
          InternalServerError("Not found target by the key.")
        case _ => InternalServerError("bad")
      }
      case false => Future.successful(Ok("is not exists"))
    }
  }

  def deleteTest = Action.async {
    val id = "abcd"
    carAdverts.delete(id).map { result =>
      Ok(result.toString)
    }
  }

  def javascriptRoutes = //cached("javascriptRoutes") {
    Action { implicit request =>
      Ok(
        JavaScriptReverseRouter("jsRoutes")(
          routes.javascript.Assets.versioned,
          routes.javascript.HomeController.list,
          routes.javascript.HomeController.listFilteredByPrice,
          routes.javascript.HomeController.listSortedByField,
          routes.javascript.HomeController.search,
          routes.javascript.HomeController.edit,
          routes.javascript.HomeController.delete,
          routes.javascript.FuelController.list,
          routes.javascript.FuelController.insert,
          routes.javascript.FuelController.delete
        )
      ).as("text/javascript")
    }

  //}

}
