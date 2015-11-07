package hu.fx.service

import hu.fx.service.api.PricesServer
import hu.fx.service.api.Quote
import hu.fx.service.yahoo.YahooPriceService
import hu.fx.service.persistencerequest.RequestSender
import hu.fx.service.persistencerequest.jms.ActiveMQHandler

class PriceServerService(priceSources: List[PriceSource]) extends PricesServer {

  lazy val sender = RequestSender(ActiveMQHandler())

  def getAllCurrencies(): List[Quote] = {

    val allQuotes = for {
      priceSource <- priceSources
      val quotesFunction = priceSource.getMostFreshQuotes
      val quotes = measureFunctionRuntime(quotesFunction, ())(priceSource.getClass + ".getMostFreshQuotes()")

    } yield quotes

    allQuotes.flatten
  }

  def saveQuotes(quotes: List[Quote]) {
    val quotes = getAllCurrencies()
    sender.sendPersistenceRequest(quotes)
  }
}

object Test {
  def main(args: Array[String]) = {
    val sources: List[PriceSource] = List(YahooPriceService())
    val p = new PriceServerService(sources)
    val currencies = p.getAllCurrencies()
    p.saveQuotes(currencies)
  }
}
