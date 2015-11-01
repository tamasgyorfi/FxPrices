package hu.staticdata

import scala.concurrent.duration.DurationInt
import akka.actor.Actor
import akka.actor.Props
import akka.pattern.ask
import akka.util.Timeout
import hu.staticdata.params.ParamValues
import hu.staticdata.params.ParamsReader
import spray.http.MediaTypes._
import spray.httpx.SprayJsonSupport._
import spray.httpx.marshalling.ToResponseMarshallable.isMarshallable
import spray.json._
import spray.json.DefaultJsonProtocol._
import spray.routing.Directive.pimpApply
import spray.routing.HttpService
import spray.routing.directives.ParamDefMagnet.apply
import scala.concurrent.Await
import hu.staticdata.params.ParamsRequest
import hu.staticdata.params.EnvironmentNotFound
import hu.staticdata.params.ParamsReader
import hu.staticdata.params.ParamsActor
import hu.staticdata.params.Message

class RestActor(paramsReader: ParamsReader) extends Actor with HttpService {

  val paramsActor = context.actorOf(Props(new ParamsActor(paramsReader.getAllParameters())), "StaticDataService-properties")

  def actorRefFactory = context
  def receive = runRoute(route)

  def serveParameterRequest(env: String, params: String): JsObject = {
    implicit val timeout = Timeout(5 seconds)
    val future = paramsActor ? ParamsRequest(env, params)

    MessageConverter.write(Await.result(future, timeout.duration).asInstanceOf[Message])
  }

  def route = {
    path("params") {
      respondWithMediaType(`application/json`) {
        parameters('env, 'keys) { (env, keys) => get(complete(serveParameterRequest(env, keys))) }
      }
    }
  }
}

private object MessageConverter extends JsonFormat[Message] {
  def write(x: Message): JsObject = x match {
    case ParamValues(values)   => values.toMap.toJson.asJsObject
    case EnvironmentNotFound => """{"error": "Environment not found"}""".asJson.asJsObject
  }

  def read(value: JsValue) = value match {
    case _ => new Message {}
  }
}

object RestActor {
  def apply(paramsReader: ParamsReader) = new RestActor(paramsReader)
}