package car_adverts_service.routes.bases

import scala.reflect.ClassTag

/**
  * Created by DongHee Kim on 2019-05-09 오후 4:18.
  */
abstract class ImplementedRoute[T : ClassTag](val routeNameSeq: Seq[String], newT: => T) extends Route {
  override def execute: Unit = newT
}

