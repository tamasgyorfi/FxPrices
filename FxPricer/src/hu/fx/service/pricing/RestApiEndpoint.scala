package hu.fx.service.pricing

import hu.fx.service.messaging.RequestSender
import akka.actor.Props
import akka.actor.Actor

class RestApiEndpoint(_requestSender: RequestSender) extends CurrenciesRoute with Actor {

  def actorRefFactory = context
  def receive = runRoute(currenciesRoute)
  val requestSender = _requestSender
  val appDriver = context.actorOf(Props(new PriceEngine(requestSender)), name = "PriceEngine")

  override def preStart {
    appDriver ! ApplicationStart
  }
}

