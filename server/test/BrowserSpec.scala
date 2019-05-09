import org.scalatestplus.play._
import org.scalatestplus.play.guice.GuiceOneServerPerTest

/**
  * Created by DongHee Kim on 2019-05-08 오후 2:30.
  */

/**
  * Runs a browser test using Fluentium against a play application on a server port.
  */
class BrowserSpec extends PlaySpec
  with OneBrowserPerTest
  with GuiceOneServerPerTest
  with HtmlUnitFactory
  with ServerProvider {

  "Application" should {

    "work from within a browser" in {

      go to ("http://localhost:" + port)

      pageSource must include ("Your new application is ready.")
    }
  }
}