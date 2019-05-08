package services

import javax.inject.{Inject, Singleton}
import play.api.{Configuration, Logger, Logging}
import play.api.inject.ApplicationLifecycle
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType._
/**
  * Created by DongHee Kim on 2019-05-08 오전 5:07.
  */

@Singleton
class DynamoDbTableManager @Inject()(dp:DynamoDbProvider) extends Logging{

  import dp.client
  logger.debug("DynamoDbTableManager")
  dp.createTableAfterCheck("car_advert")('id -> S)
}
