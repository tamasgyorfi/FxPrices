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
import hu.persistence.restapi.PriceReply
import hu.persistence.restapi.MinPriceRequest
import hu.persistence.restapi.MeanPriceRequest

trait PricesRoute extends Route {

  def pricesRoute = {
    get {
      respondWithMediaType(`application/json`) {
        path("currencies" / Segment/ Segment/ Segment/"aggregate") { (source, ccy1, ccy2) =>
          {
            parameter('filter, 'date) { (theType, date) =>
              complete {
                theType.toLowerCase() match {
                  case "min" => (newWorker ? MinPriceRequest(ccy1, ccy2, source, date)).mapTo[PriceReply].map { reply => ObjMapper.objectMapper.writeValueAsString(reply) }
                  case "avg" => (newWorker ? MeanPriceRequest(ccy1, ccy2, source, date)).mapTo[PriceReply].map { reply => ObjMapper.objectMapper.writeValueAsString(reply) }
                  case "max" => (newWorker ? MaxPriceRequest(ccy1, ccy2, source, date)).mapTo[PriceReply].map { reply => ObjMapper.objectMapper.writeValueAsString(reply) }
                }
              }
            }
          }
        }
      }
    }
  }
}
