package hu.fx.service.providers.apilayer

import hu.fx.service._
import hu.fx.service.api.PriceSource
import hu.fx.service.api.Quote
import hu.fx.service.dates.DatesSupplier
import hu.fx.service.main.RequestQuote
import hu.fx.service.main.QuotesRefresh
import hu.fx.service.providers.apilayer.config.ApiLayerPriceSource
import hu.fx.service.providers.apilayer.json.QuoteJsonParser

class ApiLayerPriceService(apiEndpoint: String) extends PriceSource with ApiLayerPriceSource{

  def getMostFreshQuotes: Unit =>List[Quote] = {
    val jsonFromApiLayer = retrievePricesAsString(apiEndpoint)
    (Unit => QuoteJsonParser(DatesSupplier).parseJsonForQuotes(jsonFromApiLayer))
  }
  
  def receive = {
    case RequestQuote => {
      val quotes = measureFunctionRuntime(getMostFreshQuotes, ())("ApiLayerPriceService.getMostFreshQuotes()")
      sender ! new QuotesRefresh(quotes, PROVIDER)
    }
  }
}
