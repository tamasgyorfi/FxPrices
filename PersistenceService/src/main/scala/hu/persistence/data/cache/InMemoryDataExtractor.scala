package hu.persistence.data.cache

import java.time.LocalDate

import hu.fx.data.Quote
import hu.persistence.api.DataExtractor
import hu.persistence.api.PortfolioElement
import hu.persistence.api.QuoteComparison

class InMemoryDataExtractor extends DataExtractor {

  //TODO: some cache-backed implementation here
  
  def calculateCurrentPortfolioValue(portfolio: List[PortfolioElement], resultCurrency: String): Double = ???
  def calculateHistoricalPortfolioValue(portfolio: List[PortfolioElement], resultCurrency: String, date: LocalDate): Double = ???

  def compare(thisCcy1: String, thisCcy2: String, thatCcy1: String, thatCcy2: String, date: LocalDate): Option[QuoteComparison] = ???

  def getCurrencyPairHistory(ccy1: String, ccy2: String, from: LocalDate): List[Quote] = ???
  def getCurrencyPairHistory(ccy1: String, ccy2: String, from: LocalDate, to: LocalDate): List[Quote] = ???

  def getHighestPrice(ccy1: String, ccy2: String, date: LocalDate): Option[Quote] = ???
  def getLowestPrice(ccy1: String, ccy2: String, date: LocalDate): Option[Quote] = ???
  def getDailyMean(ccy1: String, ccy2: String, date: LocalDate): Option[Quote] = ???

}