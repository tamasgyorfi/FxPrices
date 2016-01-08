package hu.fx.service.price.apilayer

import akka.actor.actorRef2Scala
import hu.fx.data.Quote
import hu.fx.service.dates.DatesSupplier
import hu.fx.service.measureFunctionRuntime
import hu.fx.service.price.PriceSource
import hu.fx.service.price.apilayer.config.ApiLayerPriceSource
import hu.fx.service.price.apilayer.json.QuoteJsonParser
import hu.fx.service.retrievePricesAsString
import hu.monitoring.MonitoringManager
import hu.fx.service.config.QuotesRefresh
import hu.fx.service.config.RequestQuote

class ApiLayerPriceService(apiEndpoint: String) extends PriceSource with ApiLayerPriceSource {

  def getMostFreshQuotes: Unit => List[Quote] = {
    val jsonFromApiLayer = retrievePricesAsString(apiEndpoint)
    (Unit => QuoteJsonParser(DatesSupplier).parseJsonForQuotes(jsonFromApiLayer))
  }

  def receive = {
    case RequestQuote => {
      try {
        val quotes = measureFunctionRuntime(getMostFreshQuotes, ())("ApiLayerPriceService.getMostFreshQuotes()")
        sender ! new QuotesRefresh(quotes, PROVIDER)
      } catch {
        case ex: Exception => MonitoringManager.reportError(s"Error while trying to refresh quotes from source ${PROVIDER}. Exception was: ${ex}")
      }
    }
  }
}
