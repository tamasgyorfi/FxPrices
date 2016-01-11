package hu.environment.params

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

import akka.actor.Actor
import akka.actor.ActorRef
import akka.util.Timeout
import spray.http.MediaTypes._
import spray.httpx.SprayJsonSupport._
import spray.httpx.marshalling.ToResponseMarshallable.isMarshallable
import spray.json._
import spray.json.DefaultJsonProtocol._
import spray.routing.Directive.pimpApply
import spray.routing.HttpService
import spray.routing.directives.ParamDefMagnet.apply
import akka.pattern.ask


class RestApiEndpoint(paramsActor: ActorRef) extends Actor with HttpService {

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
