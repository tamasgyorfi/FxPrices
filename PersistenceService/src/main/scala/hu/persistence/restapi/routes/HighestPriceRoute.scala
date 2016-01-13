package hu.persistence.restapi.routes

import hu.persistence.restapi.CurrencyHistoryReply
import shapeless.get
import spray.routing.HttpService
import akka.actor.ActorRef
import akka.util.Timeout
import hu.persistence.restapi.CurrencyHistoryRequest
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import spray.http.MediaTypes.`application/json`
import scala.concurrent.duration.DurationInt
import akka.pattern.ask
import hu.persistence.restapi.MaxPriceRequest
import hu.persistence.restapi.MaxPriceReply


trait HighestPriceRoute extends HttpService {
  private implicit def ec = actorRefFactory.dispatcher
  private implicit val timeout = Timeout(5 seconds)

  def newWorker: ActorRef

  def maxPriceRoute = {
    get {
      respondWithMediaType(`application/json`) {
        path("maxPrice") {
          parameter('ccy1, 'ccy2, 'date) { (ccy1, ccy2, date) =>
            complete {
              (newWorker ? MaxPriceRequest(ccy1, ccy2, date)).mapTo[MaxPriceReply].map { reply => ObjMapper.objectMapper.writeValueAsString(reply) }
            }
          }
        }
      }
    }
  }
}