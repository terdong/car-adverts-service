package car_adverts_service.routes

import bases._
import org.scalajs.dom._
/**
  * Created by DongHee Kim on 2019-05-09 오후 4:49.
  */

object Global extends Route {
  override val routeNameSeq: Seq[String] = Seq("global")

  override def execute: Unit = {
    console.log("Hello Global")
  }
}
