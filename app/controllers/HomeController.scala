package controllers

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Date

import dbs.models.CarAdvert
import dbs.repositories.CarAdverts
import javax.inject._
import org.scanamo.error.ScanamoError
import play.api.Logging
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(carAdverts:CarAdverts, cc: ControllerComponents) extends AbstractController(cc) with Logging{
  implicit val ec = ExecutionContext.global
  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
    logger.debug("index")

    Ok(views.html.index("Your new application is ready."))
  }

  def insert = ???
  def edit = ???
  def delete = ???
  def list = ???


  def insertTest = Action.async{

    val uuid = java.util.UUID.randomUUID().toString

    val date = new Date()
    val sdf = new SimpleDateFormat("yyyy-MM-dd")

    carAdverts.insert(CarAdvert(uuid , "Audi A4 Avant", "gasoline",1000, true, 0, sdf.format(date))).map{
      case Some(Right(c: CarAdvert)) => Ok(s"already exists = ${c.toString}")
      case None => Ok("cool")
      case Some(Left(error: ScanamoError)) =>
        logger.error(error.toString)
        InternalServerError("error")
      case _ => InternalServerError("bad")
    }
    //Future.successful(Ok(""))
  }

}
