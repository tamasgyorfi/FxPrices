package hu.fx.service.price.yahoo

import scala.App
import hu.fx.service._
import hu.fx.data.Quote
import hu.monitoring.MonitoringManager
import hu.fx.service.price.yahoo.xml.QuoteXmlParser
import hu.fx.service.price.yahoo.config.YahooPriceSource
import hu.fx.service.price.PriceSource
import hu.fx.service.pricing.QuotesRefresh
import hu.fx.service.pricing.RequestQuote

class YahooPriceService extends YahooPriceSource with PriceSource {

  def getMostFreshQuotes: Unit => List[Quote] = {
    (Unit => QuoteXmlParser().parse(retrievePricesAsString(YAHOO_PRICES)))
  }

  def receive = {
    case RequestQuote => {
      try {
        val quotes = measureFunctionRuntime(getMostFreshQuotes, ())("YahooPriceService.getMostFreshQuotes()")
        sender ! new QuotesRefresh(quotes, PROVIDER)
      } catch {
        case ex: Exception => MonitoringManager.reportError(s"Error while trying to refresh quotes from source ${PROVIDER}. Exception was: ${ex}")
      }
    }
  }
}