package hu.fx.service

import hu.fx.service.api.PricesServer
import hu.fx.service.api.Quote
import hu.fx.service.yahoo.YahooPriceService
import hu.fx.service.persistencerequest.RequestSender
import hu.fx.service.persistencerequest.jms.ActiveMQHandler

class PriceServerService extends PricesServer {
  
  def getAllCurrencies(): List[Quote] = {
    val quotesFunction = YahooPriceService().getMostFreshQuotes
    val quotes = measureFunctionRuntime(quotesFunction, ())("YahooPriceService.getMostFreshQuotes()")
    val sender = RequestSender(ActiveMQHandler())
    
    sender.sendPersistenceRequest(quotes)
    
    quotes
  }
}

object Test  {
  def main (args : Array[String]) = {
    val x = new PriceServerService().getAllCurrencies()
  }
}
