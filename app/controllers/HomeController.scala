package controllers

import java.text.SimpleDateFormat
import java.util.Date

import com.fasterxml.uuid.{EthernetAddress, Generators}
import dbs.models.{CarAdvert, CarAdvertUpdate}
import dbs.repositories.CarAdverts
import javax.inject._
import org.scanamo.error.ScanamoError
import play.api.Logging
import play.api.mvc._
import views.html.defaultpages.error

import scala.concurrent.{ExecutionContext, Future}

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()(carAdverts: CarAdverts, cc: ControllerComponents) extends AbstractController(cc) with Logging {
  implicit val ec = ExecutionContext.global

  /**
    * Create an Action to render an HTML page with a welcome message.
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def index = Action {
    //logger.debug("index")

    Ok(views.html.pages.list())
  }

  def insert = ???

  def edit = ???

  def delete = ???

  def list = ???

  def insertTest = Action.async {
    //val uuid = TimeBasedUUID(java.util.UUID).toString
    //val uuid =  Generators.timeBasedGenerator().generate()
    val sdf = new SimpleDateFormat("yyyy-MM-dd")
    val date = sdf.format(new Date())
    //    carAdverts.insert(CarAdvert(uuid , "Audi A4 Avant", "gasoline",1000, true, None, None )).map{
    carAdverts.insert(CarAdvert.create("Audi A4 Avant", "gasoline", 1000)).map {
      case Some(c:CarAdvert) => Ok(s"already exists = ${c.toString}")
      case None => Ok("cool")
    }
    //Future.successful(Ok(""))
  }

  def updateTest = Action.async {
    val id = "abcd2"
    val carAdvertToUpdate = CarAdvertUpdate(id, title = Some("Tico"))

    carAdverts.isExist(id).flatMap{
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
}
