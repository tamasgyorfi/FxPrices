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
import hu.persistence.restapi.CurrencyHistoryRequestRange

trait CurrencyHistoryRoute extends Route {

  def currencyHistoryRoute = {
    get {
      respondWithMediaType(`application/json`) {
        path("currencies" / Segment/Segment /Segment) { (source, ccy1, ccy2) =>
          {
            parameter('date) { (date: String) =>
              complete {
                (newWorker ? CurrencyHistoryRequest(ccy1, ccy2, source, date)).mapTo[CurrencyHistoryReply].map { x => ObjMapper.objectMapper.writeValueAsString(x) }
              }
            }
          }
        }
      } ~
      respondWithMediaType(`application/json`) {
        path("currencies" / Segment/Segment /Segment) { (source, ccy1, ccy2) =>
          {
            parameter('from, 'to) { (from: String, to: String) =>
              complete {
                (newWorker ? CurrencyHistoryRequestRange(ccy1, ccy2, source, from, to)).mapTo[CurrencyHistoryReply].map { x => ObjMapper.objectMapper.writeValueAsString(x) }
              }
            }
          }
        }
      }
    }
  }
}
  
