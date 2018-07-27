package controllers

import javax.inject._

import akka.actor.ActorSystem
import com.lightbend.rp.servicediscovery.scaladsl.ServiceLocator
import play.api._
import play.api.libs.ws.WSClient
import play.api.mvc._

import scala.concurrent.Future

@Singleton
class HomeController @Inject()(cc: ControllerComponents, ws: WSClient)(implicit system: ActorSystem) extends AbstractController(cc) {
  import system.dispatcher

  val log = Logger(this.getClass)

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok("Hello")
  }

  def getCustomer(custId: String) = Action.async {
    invokeLagomService("customer-service", s"/api/v1/customers/$custId")
  }

 
  private def invokeLagomService(serviceName: String, requestUri: String): Future[Result] =
    ServiceLocator.lookupOne(serviceName, "http")
      .flatMap {
        case Some(service) =>
          val url = s"http://${service.uri.getHost}:${service.uri.getPort}$requestUri"
          ws.url(url)
            .execute()
            .map { response =>
              Ok(response.body[String])
            }

        case None =>
          log.warn(s"The service [$serviceName] is not found")
          Future.successful(InternalServerError(s"Unable to find a service called [$serviceName]"))
      }

  
}
