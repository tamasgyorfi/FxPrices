package hu.fx.service.providers

import scala.io.Source
import java.net.URL
import org.slf4s.LoggerFactory
import hu.fx.data.Quote

trait PriceSource {
  def getMostFreshQuotes: Unit => List[Quote]
}