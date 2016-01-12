package hu.persistence.data.logger

import java.time.LocalDate
import hu.fx.data.Quote
import hu.persistence.api.DataExtractor
import hu.persistence.api.PortfolioElement
import hu.persistence.api.QuoteComparison
import hu.fx.data.EmptyQuote

/**
 * This class is here for testing only!
 * 
 * Any call to this implementation will return made up values. 
 * For correct results, other implementations, like DatabaseDataHandler or InMemoryDataHandler should be used. 
 */
class LoggingDataExtractor extends DataExtractor{
  
  def calculateCurrentPortfolioValue(portfolio: List[PortfolioElement],resultCurrency: String): Double = Double.NegativeInfinity
  def calculateHistoricalPortfolioValue(portfolio: List[PortfolioElement],resultCurrency: String,date: LocalDate): Double = Double.NegativeInfinity
  
  def compare(thisCcy1: String,thisCcy2: String,thatCcy1: String,thatCcy2: String,date: LocalDate): Option[QuoteComparison] = Option.empty
  
  def getCurrencyPairHistory(ccy1: String,ccy2: String,from: LocalDate): List[Quote] = Nil
  def getCurrencyPairHistory(ccy1: String,ccy2: String,from: LocalDate,to: LocalDate): List[Quote] = Nil
  
  def getHighestPrice(ccy1: String,ccy2: String,date: LocalDate): Option[Quote] = Option.empty
  def getLowestPrice(ccy1: String,ccy2: String,date: LocalDate): Option[Quote] = Option.empty
  def getDailyMean(ccy1: String, ccy2: String, date: LocalDate): Option[Quote] = Option.empty

}