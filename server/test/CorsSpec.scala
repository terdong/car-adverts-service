import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.http.{HeaderNames, Status}
import play.api.mvc.AnyContentAsEmpty
import play.api.test.{FakeHeaders, FakeRequest}
import play.api.test.Helpers._

/**
  * Created by DongHee Kim on 2019-05-11 오후 2:33.
  */
class CorsSpec extends PlaySpec with GuiceOneAppPerSuite {

  "From the origin request, Routes" should {

    "send 404 on a bad request" in {
      route(app, FakeRequest(GET, "/nothing")).map(status(_)) mustBe Some(NOT_FOUND)
    }

    "send 200 on a good request" in {
      val request = FakeRequest(
        method = GET,
        uri = "/count",
        headers = FakeHeaders(Seq(
          HeaderNames.HOST -> "localhost:9000",
          HeaderNames.ORIGIN -> "http://example.com",
          HeaderNames.ACCESS_CONTROL_REQUEST_HEADERS -> "Origin, Accept, Content-Type",
          HeaderNames.ACCESS_CONTROL_REQUEST_METHOD -> GET
        )),
        body = AnyContentAsEmpty,
      )
      val result = route(app, request)
      result.map{ r =>
        status(r)
      } mustBe Some(OK)
    }
  }
}
