package car_adverts_service.routes

import car_adverts_service.routes.bases.{ImplementedRoute, Route}
import org.scalajs.dom._
/**
  * Created by DongHee Kim on 2019-05-09 오전 6:08.
  */
class Home {

  console.log("Hello Home")

}

object Home extends ImplementedRoute[Home](Seq("/"), new Home) {
}