import akka.stream.Materializer
import car_adverts_service.shared.Protocols
import car_adverts_service.shared.models.Fuel
import com.amazonaws.services.dynamodbv2.model.DeleteItemResult
import controllers.FuelController
import dbs.repositories.Fuels
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.libs.json.Json
import play.api.mvc.Results
import play.api.test.FakeRequest
import play.api.test.Helpers.{stubMessagesControllerComponents, _}
import services.DynamoDbProvider

import scala.concurrent.Future
/**
  * Created by DongHee Kim on 2019-05-11 오후 5:09.
  */
class FuelControllerSpec extends PlaySpec with Results with MockitoSugar with Protocols with GuiceOneAppPerSuite{

  val sampleStoredFuelList = List("gasoline", "diesel", "gas").map(Fuel(_))
  val mockDynamoDbProvider = mock[DynamoDbProvider]
  val fuels = new Fuels(mockDynamoDbProvider) {
    override def getMaxSize: Future[Int] = Future.successful(3)

    override def isExist(id: String): Future[Boolean] = Future.successful(if (id == "gasoline") true else false)

    override def getList: Future[List[Fuel]] = Future.successful(sampleStoredFuelList)

    override def findById(id: String): Future[Option[Fuel]] = Future.successful(if (id == "diesel") Some(Fuel(id)) else None)

    override def insert(m: Fuel): Future[Option[Fuel]] = Future.successful(if (sampleStoredFuelList.exists(_.name == m.name)) Some(Fuel(m.name)) else None)

    override def delete(id: String): Future[DeleteItemResult] = Future.successful(new DeleteItemResult().clearAttributesEntries)
  }

  val controller = new FuelController(fuels, stubMessagesControllerComponents())

  "FuelController#index" should {
    "return a valid result" in {
      val result = controller.index(FakeRequest())
      status(result) must equal(play.api.http.Status.OK)
    }
  }

  "FuelController#list" should {
    "return a valid result" in {
      val resultDataByJson = Json.toJson(sampleStoredFuelList)
      val result = controller.list(FakeRequest())
      contentAsJson(result) must equal(resultDataByJson)
    }
    "return a invalid result" in {
      val resultDataByJson = Json.toJson(List("gasoline", "diesel").map(Fuel(_)))
      val result = controller.list(FakeRequest())
      contentAsJson(result) mustNot equal(resultDataByJson)
    }
  }

  "FuelController#insert" should {
    implicit lazy val materializer: Materializer = app.materializer
    "return a valid result" in {
      val fuelToInsert = Fuel("hydrogen")
      val request = FakeRequest().withBody(Json.toJson(fuelToInsert))
      val result = controller.insert.apply(request)
      status(result) must equal(play.api.http.Status.OK)
    }
    "return a invalid result" in {
      val fuelToInsert = Fuel("gasoline")
      val request = FakeRequest().withBody(Json.toJson(fuelToInsert))
      val result = controller.insert.apply(request)
      status(result) must equal(play.api.http.Status.BAD_REQUEST)
    }
  }

  "FuelController#delete" should {
    implicit lazy val materializer: Materializer = app.materializer
    "return a valid result" in {
      val fuel = Fuel("gasoline")
      val request = FakeRequest().withBody(Json.toJson(fuel))
      val result = controller.delete(fuel.name).apply(request)
      status(result) must equal(play.api.http.Status.OK)
    }
    "return a invalid result" in {
      val fuel = Fuel("gold")
      val request = FakeRequest().withBody(Json.toJson(fuel))
      val result = controller.delete(fuel.name).apply(request)
      status(result) must equal(play.api.http.Status.BAD_REQUEST)
    }
  }

}
