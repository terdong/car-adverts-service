import dbs.provider.LocalDynamoDB
import org.scalatestplus.play.PlaySpec

/**
  * Created by DongHee Kim on 2019-05-08 오전 3:18.
  */
class DynamoDbTest extends PlaySpec{
  val client = LocalDynamoDB.client()



}
