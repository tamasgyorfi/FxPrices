package hu.fx.service.main

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import hu.fx.service.apilayer.ApiLayerPriceService
import hu.fx.service.yahoo.YahooPriceService
import hu.fx.service.persistencerequest.RequestSender
import hu.fx.service.persistencerequest.jms.ActiveMQHandler
import scala.collection.parallel.ParMap
import hu.fx.service.api.Quote

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
  val freshQuotes: ParMap[String, List[Quote]] = ParMap.empty
  
  def receive = {
    case ApplicationStart => {
      actorList.foreach {actor => system.scheduler.schedule(0 seconds, actor.timing, actor.actorRef, RequestQuote) }
      context.become(start)
    }
  }

  def start: Receive = {
    case QuoteReply(quotes, senderName) => {
      messageSender sendPersistenceRequest quotes
      freshQuotes updated(senderName, quotes)
    }
    case QuoteApiRequest => {}
  }
}

object PricesServer extends AkkaSystem {
  def main(args: Array[String]) = {
    appDriver ! ApplicationStart
  }
}