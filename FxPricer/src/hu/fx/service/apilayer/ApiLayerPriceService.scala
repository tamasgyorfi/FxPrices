package hu.fx.service.apilayer

import hu.fx.service.PriceSource
import hu.fx.service._
import hu.fx.service.api.Quote
import hu.fx.service.apilayer.json.QuoteJsonParser
import java.net.URL
import scala.io.Source
import hu.fx.service.apilayer.config.ApiLayerPriceSource
import hu.fx.service.dates.DatesSupplier
import hu.fx.service.apilayer.config.ApiLayerPriceSource
import hu.fx.service.SchedulingInformation
import scala.actors.threadpool.TimeUnit

class ApiLayerPriceService(apiEndpoint: String) extends PriceSource with ApiLayerPriceSource{

  def getMostFreshQuotes: Unit =>List[Quote] = {
    val jsonFromApiLayer = retrievePricesAsString(apiEndpoint)
    (Unit => QuoteJsonParser(DatesSupplier).parseJsonForQuotes(jsonFromApiLayer))
  }
  
  def getSchedulingInformation = SchedulingInformation(1, TimeUnit.HOURS)
}
