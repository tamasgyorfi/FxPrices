package hu.fx.service.main

import scala.collection.parallel.ParMap
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import hu.fx.service.api.Quote
import hu.fx.service.persistencerequest.RequestSender
import hu.fx.service.persistencerequest.jms.ActiveMQHandler
import hu.fx.service.providers.yahoo.YahooPriceService
import hu.fx.service.providers.apilayer.ApiLayerPriceService
import hu.fx.service.api.SimpleQuote
import hu.fx.service.api.EmptyQuote

trait AkkaSystem {
  val system = ActorSystem("FxPrices")

  val appDriver = system.actorOf(Props[ApplicationDriver], name = "AppDriver")
  val yahooActor = system.actorOf(Props[YahooPriceService], name = "YahooPriceService")
  val apiLayerActor = system.actorOf(Props(new ApiLayerPriceService("")), name = "ApiLayerPriceService")

  val actorList: List[ActorTimingAssoc] = {
    List(new ActorTimingAssoc(yahooActor, 5 seconds))
  }
}

class ApplicationDriver extends Actor with AkkaSystem {

  lazy val messageSender = RequestSender(ActiveMQHandler())
  protected val freshQuotes: ParMap[String, List[Quote]] = ParMap.empty

  def receive = {
    case ApplicationStart => {
      actorList.foreach { actor => system.scheduler.schedule(0 seconds, actor.timing, actor.actorRef, RequestQuote) }
      context.become(start)
    }
  }

  def start: Receive = {
    case QuotesRefresh(quotes, senderName) => {
      messageSender sendPersistenceRequest quotes
      freshQuotes updated (senderName, quotes)
    }

    case AllQuotesApiRequest => {
      sender ! AllQuotesApiReply(Map[String, List[Quote]]() ++ freshQuotes)
    }

    case QuoteApiRequest(counterCurrency) => {
      val result = for ((source, quotes)<-freshQuotes) yield quotes.filter {quote => {quote equals(new SimpleQuote(counterCurrency, source))}}
      sender ! QuoteApiReply(result.toList.flatten)
    }
  }
}

object PricesServer extends AkkaSystem {
  def main(args: Array[String]) = {
    appDriver ! ApplicationStart
  }
}
