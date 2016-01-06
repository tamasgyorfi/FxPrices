package hu.fx.service.providers.yahoo

import scala.App
import hu.fx.service._
import hu.fx.service.api.PriceSource
import hu.fx.service.main.RequestQuote
import hu.fx.service.main.QuotesRefresh
import hu.fx.service.providers.yahoo.xml.QuoteXmlParser
import hu.fx.service.providers.yahoo.config.YahooPriceSource
import hu.fx.data.Quote
import hu.monitoring.MonitoringManager

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