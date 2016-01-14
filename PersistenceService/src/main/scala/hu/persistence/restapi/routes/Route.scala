package hu.persistence.restapi.routes

import spray.routing.HttpService
import akka.util.Timeout
import akka.actor.ActorRef
import scala.concurrent.duration.DurationInt

trait Route extends HttpService {
  implicit def ec = actorRefFactory.dispatcher
  implicit val timeout = Timeout(5 seconds)

  def newWorker: ActorRef

}