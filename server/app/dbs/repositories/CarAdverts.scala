package dbs.repositories

import java.util

import car_adverts_service.shared.Protocols
import car_adverts_service.shared.models.{CarAdvert, CarAdvertToUpdate}
import cats.data.NonEmptyList
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync
import com.amazonaws.services.dynamodbv2.model.{AttributeValue, QueryResult}
import javax.inject.{Inject, Singleton}
import org.scanamo.ops.ScanamoOps
import org.scanamo.request.{ScanamoQueryOptions, ScanamoQueryRequest}
import org.scanamo.syntax._
import org.scanamo.{ScanamoAsync, ScanamoFree, SecondaryIndex, Table}
import services.DynamoDbProvider
import org.scanamo.auto._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by DongHee Kim on 2019-05-08 오전 3:06.
  */

@Singleton
class CarAdverts @Inject()(val dp: DynamoDbProvider) extends Repositories[CarAdvert] with Protocols{

  val client: AmazonDynamoDBAsync = dp.client

  val table = Table[CarAdvert]("car_advert")
  val priceIndex: SecondaryIndex[CarAdvert] = table.index("price-index")

  implicit class ScanamoAsyncExec[A](val ops: ScanamoOps[A]) {
    def exec = ScanamoAsync.exec(client)(ops)
  }

  override def getMaxSize = {
    val r = table.scan().map(_.size)
    r.exec
  }

  def getListFilterByPrice(price: Int, offsetOptions: Option[(String, String)]) = {
    import scala.collection.JavaConverters._
    val queryOptions: ScanamoQueryOptions = ScanamoQueryOptions.default.copy(limit = Some(20), exclusiveStartKey = offsetOptions match {
      case Some(t) =>
        val map = ('sort -> 0 and 'id -> t._1).asAVMap ++ ('price -> t._2.toInt).asAVMap
        Some(map.asJava)
      case None => None
    })
    val query = ('sort -> 0 and 'price >= price).descending
    val queryResult: ScanamoOps[QueryResult] = ScanamoOps.query(ScanamoQueryRequest(table.name, Some("price-index"), query, queryOptions))
    val r = queryResult.map(extractLimitedList(_)(m => (m.get("id").getS, m.get("price").getN)))
    r.exec
  }

  override def isExist(id: String) = {
    table.query('sort -> 0 and 'id -> id).map(_.exists(_.isRight)).exec
  }

  private def extractLimitedList[T](sr: QueryResult)(evaluatedKey: util.Map[String, AttributeValue] => T) = {
    import scala.collection.JavaConverters._
    val items: util.List[util.Map[String, AttributeValue]] = sr.getItems
    val lastEvaluatedKey = Option(sr.getLastEvaluatedKey).map(evaluatedKey /*_.get("id").getS*/)
    val result = (items.asScala.map(ScanamoFree.read[CarAdvert]).toList.flatMap(_.toOption), lastEvaluatedKey)
    result
  }

  def getLimitedList(offsetKey: Option[String], number: Int = 20) = {
    val r = offsetKey
      .map(offset => table.from('sort -> 0 and 'id -> offset).limit(number))
      .getOrElse(table.limit(number))
      .query0('sort -> 0).map(extractLimitedList(_)(_.get("id").getS))
    r.exec
  }

  override def getList = {
    val r = table.scan.map(_.flatMap(_.toOption).sortBy(_.id))
    r.exec
  }

  override def findById(id: String) = {
    //    val r = table.query('id -> id).map(_.headOption)
    val r = table.get('sort -> 0 and 'id -> id).map(_.flatMap(_.toOption))
    r.exec
  }

  override def insert(c: CarAdvert) = {
    val r = table.put(c).map(_.flatMap(_.toOption))
    r.exec
  }

  override def insertAll(set: Set[CarAdvert]) = {
    val r = table.putAll(set)
    r.exec
  }

  def update(id: String, ca: CarAdvertToUpdate) = {
    val updates = List(
      ca.title.map(t => set('title -> t)),
      ca.fuel.map(f => set('fuel -> f)),
      ca.price.map(p => set('price -> p)),
      ca.newThing.map(n => set('newThing -> n)),
      Some(ca.mileage.map(m => set('mileage -> m)).getOrElse(remove('mileage))),
      Some(ca.firstRegistration.map(f => set('firstRegistration -> f)).getOrElse(remove('firstRegistration)))
    )
    val r = NonEmptyList.fromList(updates.flatten).map(ups =>
      table.update('sort -> 0 and 'id -> id, ups.reduce)
    ).map(_.exec)
    Future.sequence(Option.option2Iterable(r)).map(_.headOption)
  }

  /*  def update(c:CarAdvert) = {
     // val r = table.update('id -> c.id)
    }*/

  override def delete(id: String) = {
    table.delete('sort -> 0 and 'id -> id).exec
  }
}