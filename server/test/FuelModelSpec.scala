import car_adverts_service.shared.models.Fuel
import dbs.repositories.Repositories
import org.mockito.ArgumentMatchers._
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
/**
  * Created by DongHee Kim on 2019-05-11 오후 4:12.
  */
class FuelModelSpec extends PlaySpec with MockitoSugar{

  "Fuels#getMaxSize" should {
    "be Future(3)" in {
      val fuels = mock[Repositories[Fuel]]
      when(fuels.getMaxSize) thenReturn Future.successful(3)

      fuels.getMaxSize.map(_ mustBe 3)
    }
  }

  "Fuels#insert" should {
    "be Future[Fuel]" in {
      val fuels = mock[Repositories[Fuel]]
      val fuelToReturn = Fuel("gas")
      when(fuels.insert(any[Fuel])) thenReturn Future.successful(Some(fuelToReturn))
      fuels.insert(Fuel("gas")).map(_ mustBe fuelToReturn)
    }
  }
}
