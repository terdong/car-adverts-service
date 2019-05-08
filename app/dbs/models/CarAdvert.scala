package dbs.models

import com.fasterxml.uuid.Generators

/**
  * Created by DongHee Kim on 2019-05-08 오전 3:07.
  */
case class CarAdvert(
                      id: String = "none",
                      title: String,
                      fuel: String,
                      price: Int,
                      newThing: Boolean,
                      mileage: Option[Int],
                      firstRegistration: Option[String]
                    )

object CarAdvert {
  def apply(
             id:String,
             title: String,
             fuel: String,
             price: Int,
             newThing: Boolean,
             mileage: Option[Int],
             firstRegistration: Option[String]
           ): CarAdvert = new CarAdvert(Generators.timeBasedGenerator().generate().toString, title, fuel, price, newThing, mileage, firstRegistration)

  def apply(
             title: String,
             fuel: String,
             price: Int,
             newThing: Boolean = true,
             mileage: Option[Int] = None,
             firstRegistration: Option[String] = None
           ): CarAdvert = new CarAdvert(Generators.timeBasedGenerator().generate().toString, title, fuel, price, newThing, mileage, firstRegistration)

  def unapply(arg: CarAdvert): Option[(String
    , String
    , String
    , Int
    , Boolean
    , Option[Int]
    , Option[String]
    )] = Some((arg.id, arg.title, arg.fuel, arg.price, arg.newThing, arg.mileage, arg.firstRegistration))

  /*def create(title: String, fuel: String, price: Int) = {
     CarAdvert(Generators.timeBasedGenerator().generate().toString, title, fuel, price, true, None, None)
   }

   def create(title: String, fuel: String, price: Int, newThing: Boolean) = {
     CarAdvert(Generators.timeBasedGenerator().generate().toString, title, fuel, price, newThing, None, None)
   }

   def create(title: String, fuel: String, price: Int,  mileage: Int, firstRegistration: String) = {
     CarAdvert(Generators.timeBasedGenerator().generate().toString, title, fuel, price, false, Some(mileage), Some(firstRegistration))
   }*/
}

/*
case class CarAdvert(
                      id:String,
                      title:String,
                      fuel:String,
                      price:Int,
                      newThing:Boolean,
                      mileage:Int,
                      firstRegistration:Date
                    )
*/
//implicit val toJson = Json.format[CarAdvert]