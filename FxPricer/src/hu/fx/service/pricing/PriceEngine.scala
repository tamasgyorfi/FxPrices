package hu.fx.service.pricing

import scala.collection.parallel.ParMap
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import akka.actor.Actor
import akka.actor.actorRef2Scala
import hu.fx.data.Quote
import hu.fx.data.SimpleQuote
import hu.fx.service.messaging.PersistenceRequestSender
import hu.fx.service.price.PriceServices
import hu.fx.service.messaging.RequestSender
import hu.fx.service.price.WebPriceServices
import hu.fx.service.config.RequestQuote
import hu.fx.service.config.QuotesRefresh
import hu.fx.service.config.QuoteApiRequest
import hu.fx.service.config.QuoteApiReply
import hu.fx.service.config.ApplicationStart
import hu.fx.service.config.AllQuotesApiRequest
import hu.fx.service.config.AllQuotesApiReply

class PriceEngine(requestSender: RequestSender) extends Actor with WebPriceServices{

  protected val freshQuotes: ParMap[String, List[Quote]] = ParMap.empty

  def receive = {
    case ApplicationStart => {
      getPriceServices().foreach { actor => context.system.scheduler.schedule(0 seconds, actor.timing, actor.actorRef, RequestQuote) }
      context.become(start)
    }
  }

  def start: Receive = {
    case QuotesRefresh(quotes, senderName) => {
      freshQuotes updated (senderName, quotes)
      requestSender sendRequest quotes
    }

    case AllQuotesApiRequest => {
      sender ! AllQuotesApiReply(Map[String, List[Quote]]() ++ freshQuotes)
    }

    case QuoteApiRequest(currency, counterCurrency) => {
      val result = for ((source, quotes) <- freshQuotes) yield quotes.filter { quote => { new SimpleQuote(counterCurrency, source)(currency) equals quote } }
      sender ! QuoteApiReply(result.toList.flatten)
    }
  }
}
