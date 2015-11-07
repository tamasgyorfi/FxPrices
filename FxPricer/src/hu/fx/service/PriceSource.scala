package hu.fx.service

import hu.fx.service.api.Quote
import scala.io.Source
import java.net.URL
import org.slf4s.LoggerFactory

trait PriceSource {
  def getMostFreshQuotes: Unit => List[Quote]
  def getSchedulingInformation: SchedulingInformation
}