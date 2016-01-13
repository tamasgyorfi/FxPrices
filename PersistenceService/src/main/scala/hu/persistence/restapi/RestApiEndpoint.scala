package hu.persistence.restapi

import scala.concurrent.duration.DurationInt
import akka.actor.Actor
import akka.util.Timeout
import spray.routing.Directive.pimpApply
import spray.routing.HttpService
import spray.routing.directives.ParamDefMagnet.apply
import akka.actor.Props
import akka.actor.ActorRef
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import hu.persistence.api.DataExtractor
import spray.httpx.marshalling.ToResponseMarshallable.isMarshallable
import spray.http.MediaTypes.`application/json`
import akka.pattern.ask

trait RestApiRoute extends HttpService {
  private object Mapper {
    val objectMapper = {
      val mapper = new ObjectMapper
      mapper.registerModule(DefaultScalaModule)
      mapper
    }
  }

  private implicit def ec = actorRefFactory.dispatcher
  private implicit val timeout = Timeout(5 seconds)

  def newWorker: ActorRef

  def apiRoute = {
    get {
      respondWithMediaType(`application/json`) {
        path("currencyPairHistory") {
          parameter('ccy1, 'ccy2, 'date) { (ccy1, ccy2, date) =>
            complete {
              (newWorker ? CurrencyHistoryRequest(ccy1, ccy2, date)).mapTo[CurrencyHistoryReply].map { x => Mapper.objectMapper.writeValueAsString(x) }
            }
          }
        }
      }
    }
  }
}

class RestApiEndpoint(dataExtractor: DataExtractor) extends RestApiRoute with Actor {

  implicit def actorRefFactory = context

  def newWorker: ActorRef = {
    context.actorOf(Props(new WorkerActor(dataExtractor)))
  }

  def receive = runRoute(apiRoute)
}
