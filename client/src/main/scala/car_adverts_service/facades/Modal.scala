package car_adverts_service.facades

import org.scalajs.dom.Element

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal

/**
  * Created by DongHee Kim on 2019-05-10 오전 12:38.
  */
@js.native
@JSGlobal("Modal")
class Modal(el:Element)  extends js.Object {
  def show(): Unit = js.native
  def hide(): Unit = js.native
  def toggle(): Unit = js.native
  def setContent(): Unit = js.native
  def update(): Unit = js.native
}
