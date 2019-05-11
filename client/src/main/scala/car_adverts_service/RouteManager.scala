package car_adverts_service

import car_adverts_service.routes.bases.Route
import org.scalajs.dom.console

import scala.collection.mutable

/**
  * Created by DongHee Kim on 2017-12-05 005.
  */
object RouteManager {

  private val globalKey = "global"

  private val regex = "/[0-9]{1,}/".r
  private val replacingStr = "/#/"

  private val notFoundRoute = new Route{
    override val routeNameSeq: Seq[String] = Seq("error")

    override def execute = {
      console.warn("could not find the js code for that route")
      //window.location.href = "/"
    }
  }

  val routeMap = mutable.HashMap.empty[String, Route]

  def registerRoute(route: Route) = {
    route.routeNameSeq.map(routeName => routeMap += (routeName -> route))
  }

  def runRoute(path: String) = {
    routeMap.getOrElse(globalKey, notFoundRoute).execute
    routeMap.getOrElse(regex replaceAllIn (path, replacingStr), notFoundRoute).execute
  }
}
