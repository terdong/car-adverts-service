package car_adverts_service.shared.models

/**
  * Created by DongHee Kim on 2019-05-08 오후 5:20.
  */

case class CarAdvertToUpdate(
                      title: Option[String] = None,
                      fuel: Option[String] = None,
                      price: Option[Int] = None,
                      newThing: Option[Boolean] = None,
                      mileage: Option[Int] = None,
                      firstRegistration: Option[String] = None
                    )
