package com.co

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}
import play.api.libs.json.{Format, Json}

case class Customer(id: Option[Long], custName: String)

object Customer{
  implicit val format:Format[Customer] = Json.format
}

trait CustomerService extends Service {
  def getCustomer(custId: String): ServiceCall[NotUsed, Customer]

  override final def descriptor: Descriptor = {
    import Service._
    named("customer-service")
      .withCalls(restCall(Method.GET, "/api/v1/customers/:custId", getCustomer _))
      .withAutoAcl(true)
  }
}
