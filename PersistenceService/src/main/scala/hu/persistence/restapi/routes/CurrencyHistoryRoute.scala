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

trait CurrencyHistoryRoute extends HttpService {
  private implicit def ec = actorRefFactory.dispatcher
  private implicit val timeout = Timeout(5 seconds)

  def newWorker: ActorRef

  def currencyHistoryRoute = {
    get {
      respondWithMediaType(`application/json`) {
        path("currencyPairHistory") {
          parameter('ccy1, 'ccy2, 'date) { (ccy1, ccy2, date) =>
            complete {
              (newWorker ? CurrencyHistoryRequest(ccy1, ccy2, date)).mapTo[CurrencyHistoryReply].map { x => ObjMapper.objectMapper.writeValueAsString(x) }
            }
          }
        }
      }
    }
  }
}
  
