package car_adverts_service.routes

import car_adverts_service.components.{SimpleAjax, SimpleAlert}
import car_adverts_service.facades.Modal
import car_adverts_service.routes.bases._
import car_adverts_service.shared.models.{CarAdvert, CarAdvertToUpdate, Fuel}
import car_adverts_service.shared.{JsonResult, Protocols}
import com.thoughtworks.binding.Binding.{BindingSeq, Constants, Var, Vars}
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom._
import org.scalajs.dom.html.Input
import org.scalajs.dom.raw.HTMLFormElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.Dynamic.{global => g}

/**
  * Created by DongHee Kim on 2019-05-09 오전 6:08.
  */
class Home extends SimpleAjax with Protocols with SimpleAlert {
  val carAdvertsListRoute = g.jsRoutes.controllers.HomeController.list().url.toString
  val carAdvertsListFilteredByPriceRoute = {price:Int => g.jsRoutes.controllers.HomeController.listFilteredByPrice(price).url.toString}
  val carAdvertsListSortedByField = {field:String => g.jsRoutes.controllers.HomeController.listSortedByField(field).url.toString}
  val carAdvertsSearchRoute = {id: String => g.jsRoutes.controllers.HomeController.search(id).url.toString }
  val carAdvertsEditRoute = { id: String => g.jsRoutes.controllers.HomeController.edit(id).url.toString }
  val carAdvertsDeleteRoute = { id: String => g.jsRoutes.controllers.HomeController.delete(id).url.toString }
  val fuelListRoute = g.jsRoutes.controllers.FuelController.list().url.toString

  console.log("Hello Home")

  val carAdvertList = Vars.empty[CarAdvert]
  val carAdvertForEditModal = Var[Option[CarAdvert]](None)
  val fuelList = Vars.empty[Fuel]

  getElementSafelyById[HTMLFormElement]("searchForm").map{ form =>
    form.onsubmit = {e:Event =>
      e.preventDefault()
      val id = form.querySelector("input").asInstanceOf[Input].value
      get[JsonResult](carAdvertsSearchRoute(id)).map{
        case Some(jr: JsonResult) =>
          jr.isSuccess match{
            case true =>
              jr.message.map{
                _.validate[CarAdvert].map{ carAdvert =>
                  carAdvertList.value.clear()
                  carAdvertList.value += carAdvert
                }
              }
            case false =>
              showErrorAlert("Not found the Car Advert by Id")
          }
      }
    }
  }

  getElementSafelyById[HTMLFormElement]("filterForm").map{ form =>
    form.onsubmit = {e:Event =>
      e.preventDefault()
      val price = form.querySelector("input").asInstanceOf[Input].value.toInt
      getList[CarAdvert](carAdvertsListFilteredByPriceRoute(price)).map{
        case Some(list) =>
          carAdvertList.value.clear()
          carAdvertList.value ++= list
      }
    }
  }

  getList[CarAdvert](carAdvertsListRoute).map {
    case Some(list) =>
      carAdvertList.value ++= list
      getElementSafelyById("caradverts-table").map { el =>
        dom.render(el, bindCarAdvertList(carAdvertList))
      }
    //      console.log(list.mkString("\n"))
  }

  getList[Fuel](fuelListRoute).map{
    case Some(list) =>
      fuelList.value ++= list
  }

  bindRenderedDom(renderEditModal(carAdvertForEditModal))

  var modal: Option[Modal] = None
  getElementSafelyById("editCarAdvertModal").map { el =>
    modal = Some(new Modal(el))
  }

  bindRenderedDom(renderAlerts(),"listAlert")


  @dom
  def bindCarAdvertList(carAdvertList: Vars[CarAdvert]): Binding[BindingSeq[Node]] = {
    val sortEvent = {field:String => e:Event =>
      getList[CarAdvert](carAdvertsListSortedByField(field)).map{
        case Some(list) =>
          carAdvertList.value.clear()
          carAdvertList.value ++= list
      }}
    <thead class="thead-light">
      <tr>
        <th data:scope="col">#</th>
        <th data:scope="col" style="cursor:pointer" onclick={sortEvent("title")}><ins>Title↓</ins></th>
        <th data:scope="col" style="cursor:pointer" onclick={sortEvent("fuel")}><ins>Fuel↓</ins></th>
        <th data:scope="col" style="cursor:pointer" onclick={sortEvent("price")}><ins>Price↓</ins></th>
        <th data:scope="col" style="cursor:pointer" onclick={sortEvent("newThing")}><ins>New↓</ins></th>
        <th data:scope="col" style="cursor:pointer" onclick={sortEvent("mileage")}><ins>Mileage↓</ins></th>
        <th data:scope="col" style="cursor:pointer" onclick={sortEvent("firstRegistration")}><ins>First Registration↓</ins></th>
        <th data:scope="col" data:colspan="2" class="text-center">Action</th>
      </tr>
    </thead>
      <tbody>
        {
          Constants(carAdvertList.bind.zipWithIndex: _*).map(v => renderTr(v._1, v._2 + 1)).map(_.bind)
        }
      </tbody>
  }

  @dom
  def renderTr(carAdvert: CarAdvert, index: Int) = {
    <tr>
      <th data:scope="row">{index.toString}</th>
      <td>
        {carAdvert.title}
      </td>
      <td>
        {carAdvert.fuel.capitalize}
      </td>
      <td>
        {carAdvert.price.toString}
      </td>
      <td>
        {if (carAdvert.newThing) "Yes" else "No"}
      </td>
      <td>
        {carAdvert.mileage.getOrElse("").toString}
      </td>
      <td>
        {carAdvert.firstRegistration.getOrElse("")}
      </td>
      <td>
        <input type="hidden" name="carAdvertId" id="carAdvertId" value={carAdvert.id} />
        <button type="button" class="btn btn-outline-dark btn-sm"
                onclick={e: Event =>
                  carAdvertId.setAttribute("type","text")
                  carAdvertId.select()
                  document.execCommand("copy")
                  carAdvertId.blur()
                  carAdvertId.setAttribute("type","hidden")
                  val message = "Copied the id of the car advert to the clipboard."
                  showInfoAlert(message)
                  console.log(message)
                  }>Copy ID</button>
      </td>
      <td>
        <button type="button" class="btn btn-outline-info btn-sm" data:data-toggle="modal" data:data-target="#editCarAdvertModal"
                onclick={e: Event =>
                  carAdvertForEditModal.value = Some(carAdvert)
                  modal.map(_.show())}>
          Edit</button>
      </td>
    </tr>
  }

  @dom
  def renderEditModal(carAdvertOption: Var[Option[CarAdvert]]) = {
    <div class="modal fade" id="editCarAdvertModal" data:tabindex="-1" data:role="dialog" data:aria-labelledby="editCarAdvertModalLabel" data:aria-hidden="true">
      <div class="modal-dialog" data:role="document">
        {carAdvertOption.bind match {
        case Some(carAdvert) =>
          val sendingData = Var[CarAdvertToUpdate](CarAdvertToUpdate())
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="editCarAdvertModalLabel">Edit Car Advert</h5>
              <button type="button" class="close" data:data-dismiss="modal" data:aria-label="Close">
                <span data:aria-hidden="true">
                  &times;
                </span>
              </button>
            </div>
            <div class="modal-body">
              <form id="editForm" onsubmit={e: Event =>
                e.preventDefault()
                sendingData.value.newThing.map { newThing =>
                  sendingData.value = if (newThing) {
                    sendingData.value.copy(
                      mileage = None,
                      firstRegistration = None)
                  } else {
                    sendingData.value.copy(
                      mileage = Some(document.getElementById("mileage").asInstanceOf[Input].value toInt),
                      firstRegistration = Some(document.getElementById("firstRegistration").asInstanceOf[Input].value))
                  }
                }
                put[CarAdvertToUpdate](
                  carAdvertsEditRoute(carAdvert.id),
                  sendingData.value
                ).map {
                  case Left(errorMessage) =>
                    //showInfoAlert("Failed to edit this car advert.")
                    showErrorAlert(errorMessage)
                  case Right(jr: JsonResult) =>
                    console.log(jr.isSuccess)
                    showInfoAlert("Success to edit this car advert.")
                    jr.message.map{ jv =>
                      jv.validate[CarAdvert].map(ca => carAdvertList.value(carAdvertList.value.indexWhere(_.id == ca.id)) = ca)
                    }
                    modal.map(_.hide())
                }}>
                <input type="hidden" name="id" value={carAdvert.id}/>
                <div class="form-group">
                  <label for="recipient-name" class="col-form-label">Title</label>
                  <input type="text" class="form-control" id="title" name="title" value={carAdvert.title} data:required="required"
                         oninput={event: Event => sendingData.value = sendingData.value.copy(title = Some(title.value))}/>
                </div>
                <div class="form-group">
                  <label for="message-text" class="col-form-label">Fuel</label>
                  <select class="custom-select" name="fuel" id="fuel"
                          onchange={event: Event => sendingData.value = sendingData.value.copy(fuel = Some(fuel.value.toLowerCase))}>
                    {
                    val carAdvertFuel = carAdvert.fuel
                    for(fuel <- fuelList)yield{
                      <option selected={carAdvertFuel==fuel.name} value={fuel.name}>{fuel.name.capitalize}</option>
                      }
                    }
                  </select>
                </div>
                <div class="form-group">
                  <label for="message-text" class="col-form-label">Price</label>
                  <div class="input-group">
                    <div class="input-group-prepend">
                      <div class="input-group-text">€</div>
                    </div>
                    <input type="number" class="form-control" id="price" name="price" data:minlength="0" value={carAdvert.price.toString} data:required="required"
                           oninput={event: Event => sendingData.value = sendingData.value.copy(price = Some(price.value.toInt))}/>
                  </div>
                </div>
                <div class="form-group">
                  <label for="message-text" class="col-form-label">New</label>
                  <div class="custom-control custom-checkbox">
                    {val checkbox = <input type="checkbox" class="custom-control-input" id="newThing" name="newThing"
                                           onchange={event: Event =>
                                             val isChecked = !(newThing.value.toBoolean)
                                             newThing.value = isChecked.toString
                                             sendingData.value = sendingData.value.copy(newThing = Some(newThing.value.toBoolean))
                                           }/>.asInstanceOf[Input]
                  checkbox.value = carAdvert.newThing.toString
                  if (carAdvert.newThing) {
                    checkbox.setAttribute("checked", "checked")
                  } else {
                    checkbox.removeAttribute("checked")
                  }
                  checkbox}<label class="custom-control-label" data:for="newThing">If it is a used car, please uncheck.</label>
                  </div>
                </div>
                <div class="form-group">
                  <label for="message-text" class="col-form-label">Mileage</label>
                  <div class="input-group">
                    <input type="number" class="form-control" id="mileage" name="mileage" data:minlength="0" value={carAdvert.mileage.getOrElse("").toString} data:required="required"
                           disabled={sendingData.bind.newThing.getOrElse(carAdvert.newThing)}/>
                    <div class="input-group-prepend">
                      <div class="input-group-text">km</div>
                    </div>
                  </div>
                </div>
                <div class="form-group">
                  <label for="message-text" class="col-form-label">First Registration</label>
                  <input type="date" class="form-control" id="firstRegistration" name="firstRegistration" value={carAdvert.firstRegistration.getOrElse("")} data:required="required"
                         disabled={sendingData.bind.newThing.getOrElse(carAdvert.newThing)}/>
                </div>
              </form>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-danger mr-auto" onclick={e: Event =>
             delete[JsonResult](carAdvertsDeleteRoute(carAdvert.id)).map{
               case Some(jr:JsonResult) =>
                 console.log(jr.isSuccess)
                 val carAdvertBuffer = carAdvertList.value
                 carAdvertBuffer.remove(carAdvertBuffer.indexWhere(_.id == carAdvert.id))
                 showInfoAlert("Success to delete this car advert.")
                 modal.map(_.hide())
               case None => showErrorAlert("Failed to delete this car advert.")
             }
              }>Delete</button>
              <button type="button" class="btn btn-secondary" data:data-dismiss="modal">Close</button>
              <button type="submit" class="btn btn-primary" data:form="editForm">Edit</button>
            </div>
            <div class="container">
              <div class="row">
                <div class="col-sm">
                  {renderAlerts().bind}
                </div>
              </div>
            </div>
          </div>
        case None => <!-- empty content -->
      }}
      </div>
    </div>
  }
}

object Home extends ImplementedRoute[Home](Seq("/"), new Home)