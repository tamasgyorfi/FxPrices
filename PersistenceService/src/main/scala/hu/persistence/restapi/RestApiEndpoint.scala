package hu.persistence.restapi

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props
import hu.persistence.api.DataExtractor
import hu.persistence.restapi.routes.CurrencyHistoryRoute
import hu.persistence.restapi.routes.HighestPriceRoute

class RestApiEndpoint(dataExtractor: DataExtractor) extends CurrencyHistoryRoute with HighestPriceRoute with Actor {

  implicit def actorRefFactory = context

  def newWorker: ActorRef = {
    context.actorOf(Props(new WorkerActor(dataExtractor)))
  }

  def receive = runRoute {
    currencyHistoryRoute ~ maxPriceRoute
  }
}
