package dbs.repositories

import dbs.models.CarAdvert
import javax.inject.{Inject, Singleton}
import org.scanamo.error.DynamoReadError
import org.scanamo.ops.ScanamoOps
import org.scanamo.{Scanamo, ScanamoAsync, Table}
import services.DynamoDbProvider

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import org.scanamo.query._
import org.scanamo.syntax._
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

  def getList = {
    val r  = table.scan
    r.exec
  }

  def findById = {

  }

  def insert(c:CarAdvert) = {
    val r = table.put(c)
    r.exec
  }

  def update() = {

  }

  def delete() = {

  }




}