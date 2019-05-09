package car_adverts_service.routes

import car_adverts_service.components.SimpleAjax
import car_adverts_service.facades.Modal
import car_adverts_service.routes.bases._
import car_adverts_service.shared.Protocols
import car_adverts_service.shared.models.CarAdvert
import com.thoughtworks.binding.Binding.{BindingSeq, Vars}
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom._
import org.scalajs.dom.raw.HTMLElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.Dynamic.{global => g}

/**
  * Created by DongHee Kim on 2019-05-09 오전 6:08.
  */
class Home extends SimpleAjax with Protocols {
  val carAdvertsListRoute = g.jsRoutes.controllers.HomeController.list().url.toString

  console.log("Hello Home")

  getElementSafelyById("editCarAdvertModal").map { el =>
    el.addEventListener("show.bs.modal", { e: Event =>
      console.log("Clicked")
    })
  }

  getList[CarAdvert](carAdvertsListRoute).map {
    case Some(list) =>
      val carAdvertList = Vars.empty[CarAdvert]
      carAdvertList.value ++= list
      getElementSafelyById("caradverts-table").map { el =>
        dom.render(el, bindCarAdvertList(carAdvertList))
      }
    //      console.log(list.mkString("\n"))
  }

  bindRenderedDom(renderEditModal())

  var modal:Option[Modal] = None
  getElementSafelyById("editCarAdvertModal").map { el =>
    modal = Some(new Modal(el))
  }

  @dom
  def bindCarAdvertList(carAdvertList: Vars[CarAdvert]): Binding[BindingSeq[Node]] = {
    <thead class="thead-light">
      <tr>
        <th data:scope="col">#</th>
        <th data:scope="col">Title</th>
        <th data:scope="col">Fuel</th>
        <th data:scope="col">Price</th>
        <th data:scope="col">New</th>
        <th data:scope="col">Mileage</th>
        <th data:scope="col">First Registration</th>
        <th data:scope="col">Edit</th>
      </tr>
    </thead>
      <tbody>
        {for (carAdvert <- carAdvertList) yield {
        renderTr(carAdvert).bind
      }}
      </tbody>
  }

  @dom
  def renderTr(carAdvert: CarAdvert) = {
    <tr>
      <th data:scope="row">1</th>
      <td>
        {carAdvert.title}
      </td>
      <td>
        {carAdvert.fuel}
      </td>
      <td>
        {if (carAdvert.newThing) "Yes" else "No"}
      </td>
      <td>
        {carAdvert.price.toString}
      </td>
      <td>
        {carAdvert.mileage.getOrElse("").toString}
      </td>
      <td>
        {carAdvert.firstRegistration.getOrElse("")}
      </td>
      <td>
        <button type="button" class="btn btn-outline-info btn-sm" data:data-toggle="modal" data:data-target="#editCarAdvertModal" data:data-whatever={carAdvert.id}
                onclick={e: Event =>
                  modal.map(_.show())
                  console.log("click")}>
          Edit</button>
      </td>
    </tr>
  }


  @dom
  def renderEditModal() = {
    <div class="modal fade" id="editCarAdvertModal" data:tabindex="-1" data:role="dialog" data:aria-labelledby="editCarAdvertModalLabel" data:aria-hidden="true">
      <div class="modal-dialog" data:role="document">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="editCarAdvertModalLabel">New message</h5>
            <button type="button" class="close" data:data-dismiss="modal" data:aria-label="Close">
              <span data:aria-hidden="true">
                &times;
              </span>
            </button>
          </div>
          <div class="modal-body">
            <form>
              <div class="form-group">
                <label for="recipient-name" class="col-form-label">Recipient:</label>
                <input type="text" class="form-control" id="recipient-name"/>
              </div>
              <div class="form-group">
                <label for="message-text" class="col-form-label">Message:</label>
                <textarea class="form-control" id="message-text"></textarea>
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data:data-dismiss="modal">Close</button>
            <button type="button" class="btn btn-primary">Send message</button>
          </div>
        </div>
      </div>
    </div>
  }
}

object Home extends ImplementedRoute[Home](Seq("/"), new Home)