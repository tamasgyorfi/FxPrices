package hu.fx.service.apilayer

import hu.fx.service._
import hu.fx.service.api.PriceSource
import hu.fx.service.api.Quote
import hu.fx.service.apilayer.config.ApiLayerPriceSource
import hu.fx.service.apilayer.config.ApiLayerPriceSource
import hu.fx.service.apilayer.json.QuoteJsonParser
import hu.fx.service.dates.DatesSupplier
import hu.fx.service.main.RequestQuote
import hu.fx.service.main.QuotesRefresh

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
