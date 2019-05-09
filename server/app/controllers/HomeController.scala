package controllers

import java.text.SimpleDateFormat
import java.util.Date

import car_adverts_service.shared.models.CarAdvert
import com.fasterxml.uuid.Generators
import dbs.models.CarAdvertUpdate
import dbs.repositories.{CarAdverts, Fuels}
import javax.inject._
import org.scanamo.error.ScanamoError
import play.api.Logging
import play.api.cache.Cached
import play.api.data.Forms._
import play.api.data._
import play.api.data.validation.Constraints._
import play.api.mvc._
import play.api.routing.JavaScriptReverseRouter

import scala.concurrent.{ExecutionContext, Future}

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()(carAdverts: CarAdverts,
                               fuels: Fuels,
                               cached: Cached,
                               mcc: MessagesControllerComponents) extends MessagesAbstractController(mcc) with Logging {
  implicit val ec = ExecutionContext.global

  import play.api.libs.json._

  implicit val carAdvertFormat = Json.format[CarAdvert]
  implicit val residentReads = Json.reads[CarAdvert]
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

  /**
    * Create an Action to render an HTML page with a welcome message.
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def index = Action.async { implicit request =>
    //logger.debug("index")
    //    Ok(views.html.pages.list(List.empty[CarAdvert]))
    carAdverts.getMaxSize.map(maxSize => Ok(views.html.pages.list(maxSize)))
  }

  def createForm = Action { implicit request =>
    Ok(views.html.pages.create_form(carAdvertForm))
  }

  def create = Action.async { implicit request =>
    val result = carAdvertForm.bindFromRequest.fold(
      formWithErrors => {
        // binding failure, you retrieve the form containing errors:
        Future.successful(BadRequest(views.html.pages.create_form(formWithErrors)))
      },
      carAdvertData => {
        /* binding success, you get the actual value. */
        /*val newUser = models.User(userData.name, userData.age)
        val id      = models.User.create(newUser)*/
        carAdverts.insert(carAdvertData).map {
          case Some(c) => Ok(c.toString)
          case None => Redirect(routes.HomeController.index)
        }
      }
    )

    result
  }

  def edit = TODO

  def delete = TODO

  def list = Action.async { implicit request =>
    carAdverts.getList.map { list =>
      val json = Json.toJson(list)
      Ok(json)
    }
  }

  /*  def insertFuel = Action.async{

    }*/

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
    //Future.successful(Ok(""))
  }

  def updateTest = Action.async {
    val id = "abcd2"
    val carAdvertToUpdate = CarAdvertUpdate(id, title = Some("Tico"))

    carAdverts.isExist(id).flatMap {
      case true => carAdverts.update(carAdvertToUpdate).map {
        //case false => InternalServerError("Not found target by the key.")
        case Some(Right(c: CarAdvert)) => Ok(s"updated = ${c.toString}.")
        case None => Ok("Nothing to change.")
        case Some(Left(error: ScanamoError)) =>
          logger.error(error.toString)
          carAdverts.delete(carAdvertToUpdate.id)
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

  def javascriptRoutes = cached("javascriptRoutes") {
    Action { implicit request =>
      Ok(
        JavaScriptReverseRouter("jsRoutes")(
          routes.javascript.Assets.versioned,
          routes.javascript.HomeController.list
        )
      ).as("text/javascript")
    }
  }

}