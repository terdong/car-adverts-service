package car_adverts_service.routes

import bases._
import org.scalajs.dom._
/**
  * Created by DongHee Kim on 2019-05-09 오후 4:46.
  */
class Create {

  console.log("Hello Create")

}

object Create extends ImplementedRoute[Create](Seq("/caradverts/create"), new Create)

