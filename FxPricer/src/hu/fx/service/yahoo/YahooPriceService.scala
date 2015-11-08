package hu.fx.service.yahoo

import scala.App

import hu.fx.service._
import hu.fx.service.api.PriceSource
import hu.fx.service.api.Quote
import hu.fx.service.yahoo.config.YahooPriceSource
import hu.fx.service.main.RequestQuote
import hu.fx.service.main.QuoteReply

class YahooPriceService extends YahooPriceSource with PriceSource {

  def getMostFreshQuotes: Unit => List[Quote] = {
    (Unit => QuoteXmlParser().parse(retrievePricesAsString(YAHOO_PRICES)))
  }

  def receive = {
    case RequestQuote => {
      val quotes = measureFunctionRuntime(getMostFreshQuotes, ())("YahooPriceService.getMostFreshQuotes()")
      sender ! new QuoteReply(quotes, PROVIDER)
    }
  }
}