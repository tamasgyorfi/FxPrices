package hu.fx.service.api

trait PricesServer {
  def getAllCurrencies() : List[Quote]
  def saveQuotes(quotes : List[Quote])
}