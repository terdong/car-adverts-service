package dbs.models

import java.util.Date

/**
  * Created by DongHee Kim on 2019-05-08 오전 3:07.
  */
case class CarAdvert(
                    id:Long,
                    title:String,
                    fuel:String,
                    price:Int,
                    newThing:Boolean,
                    mileage:Int,
                    firstRegistration:Date
                    )
//implicit val toJson = Json.format[CarAdvert]