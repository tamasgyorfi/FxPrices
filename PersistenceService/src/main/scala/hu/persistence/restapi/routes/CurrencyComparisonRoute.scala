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
import hu.persistence.api.QuoteComparison
import hu.persistence.api.QuoteComparison
import hu.persistence.restapi.ComparisonRequest
import hu.persistence.restapi.ComparisonReply

trait CurrencyComparisonRoute extends Route {

  def comparisonRoute = {
    get {
      respondWithMediaType(`application/json`) {
        path("compare" / Segment) { (source) =>
          {
            parameter('q1_ccy1, 'q1_ccy2, 'q2_ccy1, 'q2_ccy2, 'date) { (firstCcy1: String, firstCcy2: String, secondCcy1: String, secondCcy2: String, date: String) =>
              complete {
                (newWorker ? ComparisonRequest(firstCcy1, firstCcy2, secondCcy1, secondCcy2, source, date)).mapTo[ComparisonReply].map { x => ObjMapper.objectMapper.writeValueAsString(x) }
              }
            }
          }
        }
      }
    }
  }
}
  
