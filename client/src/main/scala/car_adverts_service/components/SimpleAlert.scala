package car_adverts_service.components

import com.thoughtworks.binding.Binding.{BindingSeq, Vars}
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.Node


/**
  * Created by DongHee Kim on 2019-05-10 오후 4:34.
  */


trait SimpleAlert {

  val alerts = Vars.empty[Alert]

  def setAlertSchedule(alert: Alert): Unit = {
    scala.scalajs.js.timers.setTimeout(SimpleAlert.timeoutValue) {
      alerts.value -= alert
    }
  }

  def showInfoAlert(message:String) {
    val alert = Alert(SimpleAlert.infoStyle, message)
    alerts.value += alert
    setAlertSchedule(alert)
  }

  def showErrorAlert(message:String)  {
    val alert = Alert(SimpleAlert.errorStyle, message)
    alerts.value += alert
    setAlertSchedule(alert)
  }

  @dom
  def renderAlerts():Binding[Node] = {
    <div>
    {for (alert <- alerts) yield {
      <div class= {s"alert ${alert.style}"} data:role="alert">{alert.message}</div>
    }}
    </div>
  }
}

object SimpleAlert{
  val infoStyle = "alert-info"
  val errorStyle = "alert-danger"
  val timeoutValue = 3000
}

case class Alert(style:String, message:String)