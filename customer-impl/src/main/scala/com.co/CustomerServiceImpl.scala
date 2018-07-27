package com.co

import akka.NotUsed
import akka.stream.Materializer
import com.lightbend.lagom.scaladsl.api.ServiceCall

import scala.concurrent.{ExecutionContext, Future}

class CustomerServiceImpl (customerRepository: CustomerRepository)(implicit ec: ExecutionContext, mat: Materializer) extends CustomerService {
  override def getCustomer(custId: Long): ServiceCall[NotUsed, Customer] = ServiceCall { _ =>
//    customerRepository.findCustomerById(custId).map(_.getOrElse(Customer(Some(1), "test")))
      /*.flatMap {
      case Some(customer) => Future.successful(customer)
      case other => Future.successful("NotFound")
    }*/
    //Future.successful(custId.toUpperCase)
    Future.successful(Customer(Some(1), "test"))
  }
}
