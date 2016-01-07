package hu.persistence.data.logger

import hu.fx.data.Quote
import hu.persistence.api.PortfolioElement
import hu.persistence.api.QuoteComparison
import java.time.ZonedDateTime
import hu.persistence.data.DataExtractor

/**
 * This class is here for testing only!
 * 
 * Any call to this implementation will return made up values. 
 * For correct results, other implementations, like DatabaseDataHandler or InMemoryDataHandler should be used. 
 */
class LoggingDataExtractor extends DataExtractor{
  
  def calculateCurrentPortfolioValue(portfolio: List[PortfolioElement],resultCurrency: String): Double = Double.NegativeInfinity
  def calculateHistoricalPortfolioValue(portfolio: List[PortfolioElement],resultCurrency: String,date: ZonedDateTime): Double = Double.NegativeInfinity
  
  def compare(thisCcy1: String,thisCcy2: String,thatCcy1: String,thatCcy2: String,date: ZonedDateTime): QuoteComparison = null
  
  def getCurrencyPairHistory(ccy1: String,ccy2: String,from: ZonedDateTime): List[Quote] = Nil
  def getCurrencyPairHistory(ccy1: String,ccy2: String,from: ZonedDateTime,to: ZonedDateTime): List[Quote] = Nil
  
  def getHighestPrice(ccy1: String,ccy2: String,date: ZonedDateTime): Double = Double.NegativeInfinity
  def getLowestPrice(ccy1: String,ccy2: String,date: ZonedDateTime): Double = Double.NegativeInfinity
  def getDailyMean(ccy1: String, ccy2: String, date: ZonedDateTime): Double = Double.NegativeInfinity

}