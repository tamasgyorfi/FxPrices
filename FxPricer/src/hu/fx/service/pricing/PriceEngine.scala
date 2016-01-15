package hu.fx.service.pricing

import scala.collection.parallel.ParMap
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import akka.actor.Actor
import akka.actor.actorRef2Scala
import hu.fx.data.Quote
import hu.fx.data.SimpleQuote
import hu.fx.service.messaging.RequestSender
import hu.fx.service.price.WebPriceServices
import org.slf4s.LoggerFactory

class PriceEngine(requestSender: RequestSender) extends Actor with WebPriceServices {

  private val logger = LoggerFactory.getLogger(this.getClass)
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
      try {
        sender ! AllQuotesApiReply(Map[String, List[Quote]]() ++ freshQuotes, "")
      } catch {
        case ex: Exception => {
          val errormessage = s"Unable to process request. Error was ${ex}"
          logger.error(errormessage)
          sender ! AllQuotesApiReply(Map(), errormessage)
        }
      }
    }

    case QuoteApiRequest(currency, counterCurrency) => {
      try {
        val result = for ((source, quotes) <- freshQuotes) yield quotes.filter { quote => { new SimpleQuote(counterCurrency, source)(currency) equals quote } }
        sender ! QuoteApiReply(result.toList.flatten, "")
      } catch {
        case ex: Exception => {
          val errormessage = s"Unable to process request. Error was ${ex}"
          logger.error(errormessage)
          sender ! QuoteApiReply(Nil, errormessage)
        }
      }
    }
  }
}
