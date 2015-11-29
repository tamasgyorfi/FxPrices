package hu.fx.service

import hu.fx.service.api.PricesServer
import hu.fx.service.persistencerequest.RequestSender
import hu.fx.service.persistencerequest.jms.ActiveMQHandler
import hu.fx.data.Quote
import hu.fx.service.providers.PriceSource

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
