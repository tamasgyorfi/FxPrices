package hu.fx.service.pricing

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import akka.actor.Actor
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.pattern.ask
import akka.util.Timeout
import hu.fx.service.messaging.RequestSender
import spray.http.MediaTypes.`application/json`
import spray.httpx.marshalling.ToResponseMarshallable.isMarshallable
import spray.routing.Directive.pimpApply
import spray.routing.HttpService
import spray.routing.directives.ParamDefMagnet.apply
import akka.actor.ActorRef

trait CurrenciesRoute extends HttpService {
  private implicit val timeout = Timeout(5 seconds)
  def appDriver: ActorRef
  def requestSender: RequestSender

  private val objectMapper = {
    val mapper = new ObjectMapper
    mapper.registerModule(DefaultScalaModule)
    mapper
  }

  def currenciesRoute = {
    get {
      respondWithMediaType(`application/json`) {
        path("getCurrency") {
          parameter('ccy1, 'ccy2.?) { (ccy1, ccy2) => complete { handleCurrencyRequests(ccy1, ccy2) }
          }
        }
      }
    }
  }

  private def handleCurrencyRequests(ccy1: String, ccy2: Option[String]) = {
    ccy1.toUpperCase() match {
      case "ALL" => (appDriver ? AllQuotesApiRequest).mapTo[AllQuotesApiReply].map { x => objectMapper.writeValueAsString(x) }
      case _     => (appDriver ? QuoteApiRequest(ccy1, ccy2.getOrElse("EUR"))).mapTo[QuoteApiReply].map { x => objectMapper.writeValueAsString(x) }
    }
  }
}