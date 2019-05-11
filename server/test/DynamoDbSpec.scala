import org.scalatestplus.play.PlaySpec


/**
  * Created by DongHee Kim on 2019-05-08 오전 3:18.
  */
class DynamoDbSpec extends PlaySpec{
  //val client = LocalDynamoDB.client()

  "LocalDynamoDB" should {
   /* "get asynchronously" in {
      LocalDynamoDB.usingRandomTable(client)('name -> S) { t =>
        case class Farm(asyncAnimals: List[String])
        case class Farmer(name: String, age: Long, farm: Farm)
        import org.scanamo.syntax._
        import org.scanamo.auto._
        val farmers = Table[Farmer](t)

        val result = for {
          _ <- farmers.put(Farmer("McDonald", 156L, Farm(List("sheep", "cow"))))
          f <- farmers.get('name -> "McDonald")
        } yield f

        Scanamo.exec(client)(result) mustEqual (
          Some(Right(Farmer("McDonald", 156, Farm(List("sheep", "cow")))))
        )
      }
    }

    "get id" in {
      case class CarAdvert(
                            id:String,
                            title:String,
                            fuel:String,
                            price:Int,
                            newThing:Boolean,
                            mileage:Option[Int],
                            firstRegistration:Option[Date]
                          )
      LocalDynamoDB.usingRandomTable(client)('id -> S) { t =>
        //import java.util.UUID.randomUUID
        //val uuid = randomUUID().toString
        val uuid = "test"
        import org.scanamo.syntax._
        import org.scanamo.auto._
        val carAdverts = Table[CarAdvert](t)
        val result = for{
          _ <- carAdverts.put(CarAdvert(uuid , "Audi A4 Avant", "gasoline",1000, true, None, None))
          c <- carAdverts.get('id -> uuid)
        }yield c

        Scanamo.exec(client)(result) mustEqual(
          Some(Right(CarAdvert(uuid , "Audi A4 Avant", "gasoline",1000, true, None, None)))
        )
      }
    }*/
  }
}
