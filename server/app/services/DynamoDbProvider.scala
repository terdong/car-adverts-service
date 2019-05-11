package services

import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.amazonaws.services.dynamodbv2.model._
import com.amazonaws.services.dynamodbv2.{AmazonDynamoDB, AmazonDynamoDBAsync, AmazonDynamoDBAsyncClient}
import javax.inject.{Inject, Singleton}
import play.api.Configuration
import play.api.inject.ApplicationLifecycle

import scala.collection.JavaConverters._
import scala.concurrent.Future
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType._

/**
  * Created by DongHee Kim on 2019-05-08 오전 1:53.
  */
@Singleton
class DynamoDbProvider @Inject()(config: Configuration, appLifecycle: ApplicationLifecycle) {

  implicit lazy val client = AmazonDynamoDBAsyncClient
    .asyncBuilder()
    .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(config.get[String]("dynamodb.accesskey"), config.get[String]("dynamodb.secretkey"))))
    .withEndpointConfiguration(new EndpointConfiguration(config.get[String]("dynamodb.endpoint"), config.get[String]("dynamodb.region")))
    .build()

  @Deprecated
  private def client2(): AmazonDynamoDBAsync =
    AmazonDynamoDBAsyncClient
      .asyncBuilder()
      .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(config.get[String]("dynamodb.accesskey"), config.get[String]("dynamodb.secretkey"))))
      .withEndpointConfiguration(new EndpointConfiguration(config.get[String]("dynamodb.endpoint"), config.get[String]("dynamodb.region")))
      .build()

  def createTableAfterCheck(tableName: String)(attributes: (Symbol, ScalarAttributeType)*)(implicit client: AmazonDynamoDB) = {
    if (!checkTable(tableName)) {
      createTable(tableName)(attributes: _*)
    }
  }

  def createTableWithIndexAfterCheck(tableName: String, secondaryIndexName: String)(attributes: (Symbol, ScalarAttributeType)*)(secondaryIndexAttributes: (Symbol, ScalarAttributeType)*)(implicit client: AmazonDynamoDB) = {
    if (!checkTable(tableName)) {
      createTableWithIndex(tableName, secondaryIndexName, attributes.toList, secondaryIndexAttributes.toList)
    }
  }

  def createTableWithLocalIndexAfterCheck(tableName: String, localIndexName: String)(attributes: (Symbol, ScalarAttributeType)*)(secondaryIndexAttributes: (Symbol, ScalarAttributeType)*)(implicit client: AmazonDynamoDB) = {
    if (!checkTable(tableName)) {
      createTableWithLocalIndex(tableName, localIndexName, attributes.toList, secondaryIndexAttributes.toList)
    }
  }

  def getTable(tableName: String)(implicit client: AmazonDynamoDB) = {
    val db = new DynamoDB(client)
    db.getTable(tableName)
  }

  def checkTable(tableName: String)(implicit client: AmazonDynamoDB) = {
    val r = client.listTables()
    r.getTableNames.contains(tableName)
  }

  def createTable(tableName: String)(attributes: (Symbol, ScalarAttributeType)*)(implicit client: AmazonDynamoDB) =

    client.createTable(
      attributeDefinitions(attributes),
      tableName,
      keySchema(attributes),
      arbitraryThroughputThatIsIgnoredByDynamoDBLocal
    )

  def createTableWithLocalIndex(
                                 tableName: String,
                                 localIndexName: String,
                                 primaryIndexAttributes: List[(Symbol, ScalarAttributeType)],
                                 secondaryIndexAttributes: List[(Symbol, ScalarAttributeType)]
                               )(implicit client: AmazonDynamoDB) = {


    val primaryIndexSchema = keySchema(primaryIndexAttributes)
    val localSecondaryIndex = new LocalSecondaryIndex().withIndexName(localIndexName).withKeySchema(keySchema(secondaryIndexAttributes)).withProjection(new Projection().withProjectionType(ProjectionType.ALL))

    import com.amazonaws.services.dynamodbv2.model.LocalSecondaryIndex
    import java.util
    val localSecondaryIndexes = new util.ArrayList[LocalSecondaryIndex]
    localSecondaryIndexes.add(localSecondaryIndex)

    client.createTable(
      new CreateTableRequest()
        .withTableName(tableName)
        .withAttributeDefinitions(
          attributeDefinitions(primaryIndexAttributes ++ (secondaryIndexAttributes diff primaryIndexAttributes))
        )
        .withKeySchema(primaryIndexSchema)
        .withProvisionedThroughput(arbitraryThroughputThatIsIgnoredByDynamoDBLocal)
        .withLocalSecondaryIndexes(localSecondaryIndexes)
    )
  }

  def createTableWithIndex(
                            tableName: String,
                            secondaryIndexName: String,
                            primaryIndexAttributes: List[(Symbol, ScalarAttributeType)],
                            secondaryIndexAttributes: List[(Symbol, ScalarAttributeType)]
                          )(implicit client: AmazonDynamoDB) =
    client.createTable(
      new CreateTableRequest()
        .withTableName(tableName)
        .withAttributeDefinitions(
          attributeDefinitions(primaryIndexAttributes ++ (secondaryIndexAttributes diff primaryIndexAttributes))
        )
        .withKeySchema(keySchema(primaryIndexAttributes))
        .withProvisionedThroughput(arbitraryThroughputThatIsIgnoredByDynamoDBLocal)
        .withGlobalSecondaryIndexes(
          new GlobalSecondaryIndex()
            .withIndexName(secondaryIndexName)
            .withKeySchema(keySchema(secondaryIndexAttributes))
            .withProvisionedThroughput(arbitraryThroughputThatIsIgnoredByDynamoDBLocal)
            .withProjection(new Projection().withProjectionType(ProjectionType.ALL))
        )
    )

  def deleteTable(tableName: String)(implicit client: AmazonDynamoDB) =
    client.deleteTable(tableName)

  def withTable[T](tableName: String)(attributeDefinitions: (Symbol, ScalarAttributeType)*)(
    thunk: => T
  )(implicit client: AmazonDynamoDB): T = {
    createTable(tableName)(attributeDefinitions: _*)
    val res = try {
      thunk
    } finally {
      client.deleteTable(tableName)
      ()
    }
    res
  }

  def withRandomTable[T](attributeDefinitions: (Symbol, ScalarAttributeType)*)(
    thunk: String => T
  )(implicit client: AmazonDynamoDB): T = {
    var created: Boolean = false
    var tableName: String = null
    while (!created) {
      try {
        tableName = java.util.UUID.randomUUID.toString
        createTable(tableName)(attributeDefinitions: _*)
        created = true
      } catch {
        case e: ResourceInUseException =>
      }
    }

    val res = try {
      thunk(tableName)
    } finally {
      client.deleteTable(tableName)
      ()
    }
    res
  }

  def usingTable[T](tableName: String)(attributeDefinitions: (Symbol, ScalarAttributeType)*)(
    thunk: => T
  )(implicit client: AmazonDynamoDB): Unit = {
    withTable(tableName)(attributeDefinitions: _*)(thunk)
    ()
  }

  def usingRandomTable[T](attributeDefinitions: (Symbol, ScalarAttributeType)*)(
    thunk: String => T
  )(implicit client: AmazonDynamoDB): Unit = {
    withRandomTable(attributeDefinitions: _*)(thunk)
    ()
  }

  def withTableWithSecondaryIndex[T](tableName: String, secondaryIndexName: String)(
    primaryIndexAttributes: (Symbol, ScalarAttributeType)*
  )(secondaryIndexAttributes: (Symbol, ScalarAttributeType)*)(
                                      thunk: => T
                                    )(implicit client: AmazonDynamoDB): T = {
    createTableWithIndex(
      tableName,
      secondaryIndexName,
      primaryIndexAttributes.toList,
      secondaryIndexAttributes.toList
    )
    val res = try {
      thunk
    } finally {
      client.deleteTable(tableName)
      ()
    }
    res
  }

  def withRandomTableWithSecondaryIndex[T](primaryIndexAttributes: (Symbol, ScalarAttributeType)*)(secondaryIndexAttributes: (Symbol, ScalarAttributeType)*)(
    thunk: (String, String) => T
  )(implicit client: AmazonDynamoDB): T = {
    var tableName: String = null
    var indexName: String = null
    var created: Boolean = false
    while (!created) {
      try {
        tableName = java.util.UUID.randomUUID.toString
        indexName = java.util.UUID.randomUUID.toString
        createTableWithIndex(
          tableName,
          indexName,
          primaryIndexAttributes.toList,
          secondaryIndexAttributes.toList
        )
        created = true
      } catch {
        case t: ResourceInUseException =>
      }
    }

    val res = try {
      thunk(tableName, indexName)
    } finally {
      client.deleteTable(tableName)
      ()
    }
    res
  }

  private def keySchema(attributes: Seq[(Symbol, ScalarAttributeType)]) = {
    val hashKeyWithType :: rangeKeyWithType = attributes.toList
    val keySchemas = hashKeyWithType._1 -> KeyType.HASH :: rangeKeyWithType.map(_._1 -> KeyType.RANGE)
    keySchemas.map { case (symbol, keyType) => new KeySchemaElement(symbol.name, keyType) }.asJava
  }

  private def attributeDefinitions(attributes: Seq[(Symbol, ScalarAttributeType)]) =
    attributes.map { case (symbol, attributeType) => new AttributeDefinition(symbol.name, attributeType) }.asJava

  private val arbitraryThroughputThatIsIgnoredByDynamoDBLocal = new ProvisionedThroughput(1L, 1L)

  appLifecycle.addStopHook { () =>
    client.shutdown()
    Future.successful(())
  }

}