package car_adverts_service.routes.bases

/**
  * Created by DongHee Kim on 2019-05-09 오후 4:15.
  */
trait Route {
  val routeNameSeq:Seq[String]
  def execute
}
