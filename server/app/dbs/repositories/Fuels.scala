package dbs.repositories

import car_adverts_service.shared.models.{CarAdvert, Fuel}
import javax.inject.{Inject, Singleton}
import org.scanamo.ops.ScanamoOps
import org.scanamo.syntax._
import org.scanamo.{ScanamoAsync, Table}
import org.scanamo.auto._
import services.DynamoDbProvider

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by DongHee Kim on 2019-05-09 오전 12:14.
  */

@Singleton
class Fuels @Inject()(val dp: DynamoDbProvider) extends Repositories[Fuel] {

  val client = dp.client

  val table = Table[Fuel]("fuel")

  implicit class ScanamoAsyncExec[A](val ops : ScanamoOps[A]){
    def exec = ScanamoAsync.exec(client)(ops)
  }

  override def getMaxSize= {
    val r = table.scan().map(_.size)
    r.exec
  }

  override def isExist(name:String) = {
    table.query('name -> name).map(_.exists(_.isRight)).exec
  }

  override def getList = {
    val r  = table.scan.map(_.flatMap(_.toOption).sortBy(_.name))
    r.exec
  }

  override def findById(name:String) = {
    val r = table.get('name -> name).map(_.flatMap(_.toOption))
    r.exec
  }

  override def insert(f:Fuel) = {
    val r = table.put(f).map(_.flatMap(_.toOption))
    r.exec
  }

  override def delete(name:String) = {
    table.delete('name -> name).exec
  }
}