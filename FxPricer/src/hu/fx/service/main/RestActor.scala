package hu.fx.service.main

import akka.actor.Actor
import spray.routing.HttpService
import spray.http.MediaTypes._
import akka.util.Timeout
import scala.concurrent.Await
import spray.json._
import scala.concurrent.duration.DurationInt
import akka.actor.Props
import akka.pattern.ask
import spray.httpx.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._
import akka.actor.ActorSystem
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success
import scala.util.Failure
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import java.io.StringWriter
import hu.monitoring.MonitoringManager

object Mapper {
  def objectMapper = {
    val mapper = new ObjectMapper
    mapper.registerModule(DefaultScalaModule)
    mapper
  }
}

class RestActor extends Actor with HttpService {
  val appDriver = context.actorOf(Props[ApplicationDriverActor], name = "AppDriver")
  implicit val timeout = Timeout(5 seconds)

  def actorRefFactory = context
  def receive = runRoute(oneCurrencyRoute)

  override def preStart {
    appDriver ! ApplicationStart
  }

  def responseAsJson(response: Any) = {
    val writer = new StringWriter
    val unit = Mapper.objectMapper.writeValue(writer, response)
    writer.toString().parseJson
  }

  def exceptionAsJson(ex: Throwable) = Map("error" -> s"Error while making API call: $ex").toJson.asJsObject
  def unknownErrorAsJson = Map("error" -> s"Unknown error while making API call.").toJson.asJsObject

  def serveQuoteRequest(counterCurrency: String) = {
    val future = appDriver ? QuoteApiRequest(counterCurrency)
    onComplete(future) {
      case Success(QuoteApiReply(quotes)) => { complete(responseAsJson(quotes).asInstanceOf[JsArray]) }
      case Failure(ex) => {
        MonitoringManager.reportWarning(s"Exception while trying to respond. [QuoteApiRequest]. Exception was: {$ex}")
        complete(exceptionAsJson(ex))
      }
      case _ => { complete(unknownErrorAsJson) }
    }
  }

  def serveAllQuoteRequest() = {
    val future = appDriver ? AllQuotesApiRequest
    onComplete(future) {
      case Success(AllQuotesApiReply(quotes)) => { complete(responseAsJson(quotes).asJsObject) }
      case Failure(ex) => {
        MonitoringManager.reportWarning(s"Exception while trying to respond. [AllQuotesApiRequest]. Exception was: {$ex}")
        complete(exceptionAsJson(ex))
      }
      case _ => { complete(unknownErrorAsJson) }
    }
  }

  def oneCurrencyRoute = {
    get {
      respondWithMediaType(`application/json`) {
        path("getCurrency") {
          parameter('currency) { counterCurrency =>
            counterCurrency match {
              case "all" => serveAllQuoteRequest()
              case _     => serveQuoteRequest(counterCurrency)
            }
          }
        }
      }
    }
  }
}