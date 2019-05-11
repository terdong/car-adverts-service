package car_adverts_service.routes

import car_adverts_service.components.{SimpleAjax, SimpleAlert}
import car_adverts_service.routes.bases.ImplementedRoute
import car_adverts_service.shared.{JsonResult, Protocols}
import car_adverts_service.shared.models.Fuel
import com.thoughtworks.binding.Binding.{BindingSeq, Constants, Vars}
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.Dynamic.{global => g}

/**
  * Created by DongHee Kim on 2019-05-11 오전 1:02.
  */
class Fuels extends SimpleAjax with Protocols with SimpleAlert {
  val fuelListRoute = g.jsRoutes.controllers.FuelController.list().url.toString
  val fuelInsertRoute = g.jsRoutes.controllers.FuelController.insert().url.toString
  val fuelEditRoute = { id: String => g.jsRoutes.controllers.FuelController.edit(id).url.toString }
  val fuelDeleteRoute = { id: String => g.jsRoutes.controllers.FuelController.delete(id).url.toString }

  console.log("Hello Fuel")

  bindRenderedDom(renderAlerts(), "listAlert")

  val fuelList = Vars.empty[Fuel]
  //  val carAdvertForEditModal = Var[Option[CarAdvert]](None)

  getList[Fuel](fuelListRoute).map {
    case Some(list) =>
      fuelList.value ++= list
      getElementSafelyById("fuels-table").map { el =>
        dom.render(el, bindFuelList(fuelList))
      }
    //      console.log(list.mkString("\n"))
  }

  //bindRenderedDom(renderEditModal(carAdvertForEditModal))

  /*  var modal: Option[Modal] = None
    getElementSafelyById("editCarAdvertModal").map { el =>
      modal = Some(new Modal(el))
    }*/

  def resultEvent = { message: String => privateEvent: (() => Unit) =>
    either: Either[String, JsonResult] =>
      either match {
        case Left(str) => showErrorAlert(str)
        case Right(jsResult) => jsResult.isSuccess match {
          case true =>
            showInfoAlert(s"Succeed to ${message} the fuel.")
            privateEvent
          case false => showErrorAlert(s"Failed to ${message} the fuel.")
        }
      }
  }

  @dom
  def bindFuelList(fuelList: Vars[Fuel]): Binding[BindingSeq[Node]] = {
    <thead class="thead-light">
      <tr>
        <th data:scope="col">#</th>
        <th data:scope="col">Fuel</th>
        <th data:scope="col" class="text-center">Action</th>

      </tr>
    </thead>
      <tbody>
        {<tr>
        <th data:scope="row">New</th>
        <td>
          <form id="createForm" onsubmit={e: Event =>
            e.preventDefault()
            val fuelToSend = Fuel(fuelNameToCreate.value)
            postWithJsonResult[Fuel](fuelInsertRoute, fuelToSend).map {
              case Left(str) => showErrorAlert(str)
              case Right(jsResult) => jsResult.isSuccess match {
                case true =>
                  showInfoAlert(s"Succeed to create the fuel.")
                  fuelList.value += fuelToSend
                  fuelNameToCreate.value = ""
                case false => showErrorAlert(s"Failed to create the fuel.")
              }
            }}>
            <input class="form-control form-control-sm" type="text" id="fuelNameToCreate" data:required="required" data:minlength="2"/>
          </form>
        </td>
        <td>
          <button type="submit" class="btn btn-outline-success btn-sm" data:form="createForm">Create</button>
        </td>
      </tr>}{Constants(fuelList.bind.zipWithIndex: _*).map(v => renderTr(v._1, v._2 + 1)).map(_.bind)}
      </tbody>
  }

  @dom
  def renderTr(fuel: Fuel, index: Int) = {
    <tr>
      <th data:scope="row">
        {index.toString}
      </th>
      <td>
        {fuel.name}
      </td>
      <td>
        <button type="button" class="btn btn-outline-danger btn-sm"
                onclick={e: Event =>
                  delete(fuelDeleteRoute(fuel.name), fuel).map {
                    case Left(str) => showErrorAlert(str)
                    case Right(jsResult) => jsResult.isSuccess match {
                      case true =>
                        showInfoAlert(s"Succeed to delete the fuel.")
                        fuelList.value.remove(fuelList.value.indexWhere(_.name == fuel.name))
                      case false => showErrorAlert(s"Failed to delete the fuel.")
                    }
                  }}>
          Delete</button>
      </td>
    </tr>
  }

}

object Fuels extends ImplementedRoute[Fuels](Seq("/fuels"), new Fuels)