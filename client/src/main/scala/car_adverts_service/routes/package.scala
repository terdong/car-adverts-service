package car_adverts_service
import java.util.concurrent.atomic.AtomicInteger

import com.thoughtworks.binding.Binding.BindingSeq
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.document
import org.scalajs.dom._
import org.scalajs.dom.raw.{Element, Node}
import scalatags.JsDom.all._
/**
  * Created by DongHee Kim on 2019-05-09 오후 5:07.
  */

package object routes {
  import com.thoughtworks.binding.Binding
  implicit def makeIntellijHappy(x: scala.xml.Node): Binding[org.scalajs.dom.raw.Node] = ???

  sealed class DefaultsTo[A, B]
  trait LowPriorityDefaultsTo {
    implicit def overrideDefault[A,B] = new DefaultsTo[A,B]
  }
  object DefaultsTo extends LowPriorityDefaultsTo {
    implicit def default[B] = new DefaultsTo[B, B]
  }

  def getElementSafelyById[T <: Element](id: String)(implicit e: T DefaultsTo Element): Option[T] = {
    document.getElementById(id) match {
      case el: T => Some(el)
      case other =>
        console.warn(s"Element with $id is $other")
        None
    }
  }

  def getElementsSafely(elementParticulars: String): Option[NodeList] = {
    val nodeList: NodeList = document.querySelectorAll(elementParticulars)
    if(nodeList.length > 0){
      Some(nodeList)
    }else{
      console.warn(s"Elements with $elementParticulars are null")
      None
    }
  }

  def getElementSafely[T <: Element](elementParticulars: String)(implicit e: T DefaultsTo Element): Option[T] = {
    val queryResult = document.querySelector(elementParticulars)
    queryResult match {
      case elem: T => Some(elem)
      case other =>
        console.warn(s"Element with $elementParticulars is $other")
        None
    }
  }

  var divCreatingCount = new AtomicInteger()
  def bindRenderedDom(renderedDom:Binding[Node], parentId:String) = {
    val createdDiv= div(id := s"div-${divCreatingCount.getAndIncrement()}-for-binding").render
    document.getElementById(parentId).appendChild(createdDiv)
    dom.render(createdDiv, renderedDom)
  }

  def bindRenderedDom(renderedDom:Binding[Node], parentNode:Element = document.body) = {
    val createdDiv= div(id := s"div-${divCreatingCount.getAndIncrement()}-for-binding").render
    parentNode.appendChild(createdDiv)
    dom.render(createdDiv, renderedDom)
  }
  def bindSeqRenderedDom(renderedDom:Binding[BindingSeq[Node]], parentNode:Element = document.body) = {
    val createdDiv= div(id := s"div-${divCreatingCount.getAndIncrement()}-for-binding").render
    parentNode.appendChild(createdDiv)
    dom.render(createdDiv, renderedDom)
  }
}
