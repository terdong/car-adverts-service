package dbs.repositories

import car_adverts_service.shared.models.{CarAdvert, CarAdvertToUpdate}
import cats.data.NonEmptyList
import javax.inject.{Inject, Singleton}
import org.scanamo.ops.ScanamoOps
import org.scanamo.syntax._
import org.scanamo.{ScanamoAsync, Table}
import services.DynamoDbProvider

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import org.scanamo.auto._
/**
  * Created by DongHee Kim on 2019-05-08 오전 3:06.
  */

@Singleton
class CarAdverts @Inject()(val dp: DynamoDbProvider) {

  val client = dp.client

  val table = Table[CarAdvert]("car_advert")

  implicit class ScanamoAsyncExec[A](val ops : ScanamoOps[A]){
    def exec = ScanamoAsync.exec(client)(ops)
  }

  def getMaxSize= {
    val r = table.scan().map(_.size)
    r.exec
  }

  def isExist(id:String) = {
    table.query('id -> id).map(_.exists(_.isRight)).exec
  }

  def getList = {
    val r  = table.scan.map(_.flatMap(_.toOption))
    r.exec
  }

  def findById(id:String) = {
//    val r = table.query('id -> id).map(_.headOption)
    val r = table.get('id -> id).map(_.flatMap(_.toOption))
    r.exec
  }

  def insert(c:CarAdvert) = {
    val r = table.put(c).map(_.flatMap(_.toOption))
    r.exec
  }

  def update(id:String, ca: CarAdvertToUpdate)= {
    val updates = List(
      ca.title.map(t => set('title -> t)),
      ca.fuel.map(f => set('fuel -> f)),
      ca.price.map(p => set('price -> p)),
      ca.newThing.map(n => set('newThing -> n)),
      ca.mileage.map(m => set('mileage -> m)),
      ca.firstRegistration.map(f => set('firstregistration -> f))
    )
    val r = NonEmptyList.fromList(updates.flatten).map(ups =>
      table.update('id -> id, ups.reduce)
    ).map(_.exec)
    Future.sequence(Option.option2Iterable(r)).map(_.headOption)
  }

/*  def update(c:CarAdvert) = {
   // val r = table.update('id -> c.id)
  }*/

  def delete(id:String) = {
    table.delete('id -> id).exec
  }




}