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

trait CurrencyHistoryRoute extends Route {

  def currencyHistoryRoute = {
    get {
      respondWithMediaType(`application/json`) {
        path("currencyPairHistory" / Segment) { (source) =>
          {
            parameter('ccy1, 'ccy2, 'date) { (ccy1: String, ccy2: String, date: String) =>
              complete {
                (newWorker ? CurrencyHistoryRequest(ccy1, ccy2, source, date)).mapTo[CurrencyHistoryReply].map { x => ObjMapper.objectMapper.writeValueAsString(x) }
              }
            }
          }
        }
      }
    }
  }
}
  
