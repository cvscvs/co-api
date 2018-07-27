package com.co

import javax.inject.Inject
import play.api.db.DBApi
import anorm.SqlParser.get
import anorm._
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}
import play.api.libs.json.{Format, Json}

import scala.collection.immutable
import scala.concurrent.Future


case class Customer(id: Option[Long] = None, customerName: String)

object Customer {
  implicit val format: Format[Customer] = Json.format
}

@javax.inject.Singleton
class CustomerRepository @Inject()(dbapi: DBApi)(implicit ec: DatabaseExecutionContext) {

  private val db = dbapi.database("default")

  private val simple = {
    get[Option[Long]]("cust.CUST_ID")~
    get[String]("cust.CUST_NAME") map{
      case id ~ custName =>
        Customer(id, custName)
    }
  }

  def findCustomerById(id:Long):Future[Option[Customer]] = Future{
    db.withConnection { implicit connection =>
      SQL"SELECT cust.* FROM TBLCUSTOMER cust WHERE cust.CUST_ID = $id".as(simple.singleOpt)

    }

  }(ec)

}

object CustomerSerializerRegistry extends JsonSerializerRegistry{
  override def serializers: immutable.Seq[JsonSerializer[_]] = List(
    JsonSerializer[Customer]
  )
}
