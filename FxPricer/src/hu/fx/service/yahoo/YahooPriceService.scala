package hu.fx.service.yahoo

import hu.fx.service._
import hu.fx.service.api.Quote
import java.net.URL
import scala.io.Source
import scala.App
import hu.fx.service.yahoo.config.YahooPriceSource
import hu.fx.service.PriceSource
import hu.fx.service.SchedulingInformation
import scala.actors.threadpool.TimeUnit
import org.slf4s.Logger
import org.slf4s.LoggerFactory

class YahooPriceService extends YahooPriceSource with PriceSource {

  def getMostFreshQuotes: Unit => List[Quote] = {
    (Unit => QuoteXmlParser().parse(retrievePricesAsString(YAHOO_PRICES)))
  }

  def getSchedulingInformation = SchedulingInformation(5, TimeUnit.MINUTES)
}

object YahooPriceService {
  def apply() = new YahooPriceService
}
