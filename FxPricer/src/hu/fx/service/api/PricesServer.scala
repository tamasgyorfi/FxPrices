package hu.fx.service.api

import hu.fx.data.Quote

trait PricesServer {
  def getAllCurrencies() : List[Quote]
  def saveQuotes(quotes : List[Quote])
}