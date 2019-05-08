package dbs.models

import java.util.Date

import com.fasterxml.uuid.Generators

/**
  * Created by DongHee Kim on 2019-05-08 오전 3:07.
  */
case class CarAdvert(
                      id: String,
                      title: String,
                      fuel: String,
                      price: Int,
                      newThing: Boolean,
                      mileage: Option[Int],
                      firstRegistration: Option[String]
                    )

object CarAdvert {
  def create(title: String, fuel: String, price: Int) = {
    CarAdvert(Generators.timeBasedGenerator().generate().toString, title, fuel, price, true, None, None)
  }

/*  def create(title: String, fuel: String, price: Int, newThing: Boolean) = {
    CarAdvert(Generators.timeBasedGenerator().generate().toString, title, fuel, price, newThing, None, None)
  }*/

  def create(title: String, fuel: String, price: Int,  mileage: Int, firstRegistration: String) = {
    CarAdvert(Generators.timeBasedGenerator().generate().toString, title, fuel, price, false, Some(mileage), Some(firstRegistration))
  }
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