package hu.persistence.data.cache

import hu.fx.data.Quote
import hu.persistence.api.PortfolioElement
import hu.persistence.api.QuoteComparison
import java.time.ZonedDateTime
import hu.persistence.api.DataExtractor

class InMemoryDataExtractor extends DataExtractor {

  //TODO: some cache-backed implementation here
  
  def calculateCurrentPortfolioValue(portfolio: List[PortfolioElement], resultCurrency: String): Double = ???
  def calculateHistoricalPortfolioValue(portfolio: List[PortfolioElement], resultCurrency: String, date: ZonedDateTime): Double = ???

  def compare(thisCcy1: String, thisCcy2: String, thatCcy1: String, thatCcy2: String, date: ZonedDateTime): QuoteComparison = ???

  def getCurrencyPairHistory(ccy1: String, ccy2: String, from: ZonedDateTime): List[Quote] = ???
  def getCurrencyPairHistory(ccy1: String, ccy2: String, from: ZonedDateTime, to: ZonedDateTime): List[Quote] = ???

  def getHighestPrice(ccy1: String, ccy2: String, date: ZonedDateTime): Double = ???
  def getLowestPrice(ccy1: String, ccy2: String, date: ZonedDateTime): Double = ???
  def getDailyMean(ccy1: String, ccy2: String, date: ZonedDateTime): Double = ???

}