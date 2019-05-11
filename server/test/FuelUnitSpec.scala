import akka.stream.Materializer
import car_adverts_service.shared.models.Fuel
import org.scalatestplus.play._
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.libs.json.Reads.minLength
import play.api.libs.json.{JsPath, Json}
import play.api.mvc.Results._
import play.api.mvc._
import play.api.test.Helpers._
import play.api.test._

/**
  * Created by DongHee Kim on 2019-05-11 오후 9:45.
  */
class FuelUnitSpec extends PlaySpec with GuiceOneAppPerSuite {

  implicit lazy val materializer: Materializer = app.materializer
  implicit lazy val Action = app.injector.instanceOf(classOf[DefaultActionBuilder])
  implicit val fuelFormat = Json.format[Fuel]

  /*  "An essential action" should {
      "can parse a JSON body" in {
        val action: EssentialAction = Action { request =>
          val value = (request.body.asJson.get \ "field").as[String]
          Ok(value)
        }

        val request = FakeRequest(POST, "/").withJsonBody(Json.parse("""{ "field": "value" }"""))

        val result = call(action, request)

        status(result) mustEqual OK
        contentAsString(result) mustEqual "value"
      }
    }*/

  "An validation action" should {
    val fuelNameReads = ((JsPath \ "name").read[String](minLength[String](2)))
    val cc = Helpers.stubControllerComponents(
      playBodyParsers = Helpers.stubPlayBodyParsers(materializer)
    )
    val action = Action(cc.parsers.json) { request =>
      request.body.validate[String](fuelNameReads).fold(
        errors => BadRequest(""),
        name => Ok("")
      )
    }
    "can valid a JSON body" in {
      val request = FakeRequest(POST, "/").withJsonBody(Json.obj("name" -> "gas"))
      val result = call(action, request)
      status(result) must equal(OK)
    }
    "can invalid a JSON body" in {
      val request = FakeRequest(POST, "/").withJsonBody(Json.obj("name" -> "g"))
      val result = call(action, request)
      status(result) must equal(BAD_REQUEST)
    }
  }
}
