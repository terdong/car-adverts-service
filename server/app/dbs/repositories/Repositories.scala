package dbs.repositories

import com.amazonaws.services.dynamodbv2.model.{BatchWriteItemResult, DeleteItemResult}

import scala.concurrent.Future

/**
  * Created by DongHee Kim on 2019-05-11 오후 4:14.
  */
trait Repositories[M] {

  def getMaxSize: Future[Int]

  def isExist(id: String): Future[Boolean]

  def getList: Future[List[M]]

  def findById(id: String): Future[Option[M]]

  def insert(m: M): Future[Option[M]]

  def delete(id: String) : Future[DeleteItemResult]

  def insertAll(set: Set[M]): Future[List[BatchWriteItemResult]] = Future.successful(List.empty[BatchWriteItemResult])
}