package hu.fx.service.pricing

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.util.Failure
import scala.util.Success
import akka.actor.Actor
import akka.actor.Props
import akka.pattern.ask
import akka.util.Timeout
import hu.fx.data.Quote
import hu.monitoring.MonitoringManager
import spray.http.MediaTypes._
import spray.httpx.SprayJsonSupport._
import spray.httpx.marshalling.ToResponseMarshallable
import spray.json._
import spray.json.DefaultJsonProtocol._
import spray.routing.HttpService
import hu.fx.service.messaging.RequestSender
import hu.monitoring.MonitoringManager
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

class RestApiEndpoint(requestSender: RequestSender) extends Actor with HttpService {
  private val appDriver = context.actorOf(Props(new PriceEngine(requestSender)), name = "PriceEngine")
  private val objectMapper = {
    val mapper = new ObjectMapper
    mapper.registerModule(DefaultScalaModule)
    mapper
  }
  private implicit val timeout = Timeout(5 seconds)

  def actorRefFactory = context
  def receive = runRoute(currenciesRoute)

  override def preStart {
    appDriver ! ApplicationStart
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
    ccy1 match {
      case "all" => (appDriver ? AllQuotesApiRequest).mapTo[AllQuotesApiReply].map { x => objectMapper.writeValueAsString(x) }
      case _     => (appDriver ? QuoteApiRequest(ccy1, ccy2.getOrElse("EUR"))).mapTo[QuoteApiReply].map { x => objectMapper.writeValueAsString(x) }
    }
  }
}