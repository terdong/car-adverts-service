package services

import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType._
import javax.inject.{Inject, Singleton}
import play.api.Logging
/**
  * Created by DongHee Kim on 2019-05-08 오전 5:07.
  */

@Singleton
class DynamoDbTableManager @Inject()(dp:DynamoDbProvider) extends Logging{

  import dp.client
  logger.debug("DynamoDbTableManager")
//  dp.createTableAfterCheck("car_advert")('id -> S)
  dp.createTableWithIndexAfterCheck("car_advert", "price-index")('sort -> N, 'id -> S)('sort -> N, 'price -> N)
  //dp.createTableWithLocalIndexAfterCheck("car_advert", "newThing-index")('id -> S, 'title ->S)('price -> N)
  dp.createTableAfterCheck("fuel")('name -> S)
}
