package car_adverts_service

import org.scalajs.dom._

object ScalaJSLauncher{

  def main(args :Array[String]): Unit = {
/*    console.log(window.location.hash)
    console.log(window.location.pathname)*/

    registerRoute(Global)
    registerRoute(Home)
    registerRoute(Dev)
    registerRoute(Inventory)
    registerRoute(Etymology)
    registerRoute(Verb)
    registerRoute(VerbPattern)
    registerRoute(Quiz)
    registerRoute(InfiniteScrolling)
    registerRoute(Settings)
    registerRoute(Search)

    runRoute(window.location.pathname);
  }
}
