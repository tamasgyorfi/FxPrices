package hu.persistence.data

import java.time.ZonedDateTime
import hu.fx.data.Quote
import hu.persistence.api.QuoteComparison
import hu.persistence.api.PortfolioElement

trait DataExtractor {
  def getCurrencyPairHistory(ccy1: String, ccy2:String, from: ZonedDateTime, to:ZonedDateTime): List[Quote]
  def getCurrencyPairHistory(ccy1: String, ccy2:String, from: ZonedDateTime): List[Quote]
  
  def getHighestPrice(ccy1: String, ccy2:String, date: ZonedDateTime): Double
  def getLowestPrice(ccy1: String, ccy2:String, date: ZonedDateTime): Double
  def getDailyMean(ccy1:String, ccy2:String, date:ZonedDateTime): Double
  
  def compare(thisCcy1:String, thisCcy2:String, thatCcy1:String, thatCcy2:String, date:ZonedDateTime): QuoteComparison
  
  def calculateCurrentPortfolioValue(portfolio: List[PortfolioElement], resultCurrency:String): Double
  def calculateHistoricalPortfolioValue(portfolio: List[PortfolioElement], resultCurrency:String, date:ZonedDateTime): Double
}