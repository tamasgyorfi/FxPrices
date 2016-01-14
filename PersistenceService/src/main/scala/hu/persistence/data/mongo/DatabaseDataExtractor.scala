package hu.persistence.data.mongo

import java.time.LocalDate
import java.time.temporal.ChronoUnit
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoCollection
import com.mongodb.casbah.commons.MongoDBObject
import hu.fx.data.FxQuote
import hu.fx.data.PmQuote
import hu.fx.data.Quote
import hu.persistence.api.DataExtractor
import hu.persistence.api.PortfolioElement
import hu.persistence.api.QuoteComparison
import hu.fx.data.EmptyQuote

class DatabaseDataExtractor(collection: MongoCollection) extends DataExtractor {

  //TODO: some database-backed implementation here
  private val DESCENDING = -1
  private val ASCENDING = 1

  def calculateCurrentPortfolioValue(portfolio: List[PortfolioElement], source: String, resultCurrency: String): Double = ???
  def calculateHistoricalPortfolioValue(portfolio: List[PortfolioElement], source: String, resultCurrency: String, date: LocalDate): Double = ???

  def compare(thisCcy1: String, thisCcy2: String, thatCcy1: String, thatCcy2: String, source: String, date: LocalDate): Option[QuoteComparison] = ???

  def getCurrencyPairHistory(ccy1: String, ccy2: String, source: String, date: LocalDate): List[Quote] = {
    val yesterday = date.minus(1, ChronoUnit.DAYS).toString()
    val tomorrow = date.plus(1, ChronoUnit.DAYS).toString()

    val selectStatement = MongoDBObject(DbColumnNames.CCY1 -> ccy1, DbColumnNames.CCY2 -> ccy2, DbColumnNames.SOURCE -> source) ++ (DbColumnNames.TIMESTAMP $gt yesterday $lt tomorrow)
    val sortStatement = MongoDBObject(DbColumnNames.TIMESTAMP -> ASCENDING)

    collection
      .find(selectStatement)
      .sort(sortStatement)
      .map(_.translate())
      .filter(_.isDefined)
      .map(_.get)
      .toList
  }

  def getCurrencyPairHistory(ccy1: String, ccy2: String, source: String, from: LocalDate, to: LocalDate): List[Quote] = ???

  def getHighestPrice(ccy1: String, ccy2: String, source: String, date: LocalDate): Option[Quote] = {
    getTopQuote(ccy1, ccy2, source, date, DESCENDING)
  }

  def getLowestPrice(ccy1: String, ccy2: String, source: String, date: LocalDate): Option[Quote] = {
    getTopQuote(ccy1, ccy2, source, date, ASCENDING)
  }
  def getDailyMean(ccy1: String, ccy2: String, source: String, date: LocalDate): Option[Quote] = ???

  private def getTopQuote(ccy1: String, ccy2: String, source: String, date: LocalDate, sortMode: Int): Option[Quote] = {
        val yesterday = date.minus(1, ChronoUnit.DAYS).toString()
    val tomorrow = date.plus(1, ChronoUnit.DAYS).toString()

    val selectStatement = MongoDBObject(DbColumnNames.CCY1 -> ccy1, DbColumnNames.CCY2 -> ccy2, DbColumnNames.SOURCE -> source) ++ (DbColumnNames.TIMESTAMP $gt yesterday $lt tomorrow)
    val sortStatement = MongoDBObject(DbColumnNames.PRICE -> sortMode)

    collection
      .find(selectStatement)
      .sort(sortStatement)
      .limit(1)
      .one()
      .translate()
  }
  
  implicit class MongoResutToQuote(dbObject: DBObject) {
    def translate(): Option[Quote] = {
      if (dbObject == null) {
        Option.empty
      } else if (dbObject.get(DbColumnNames.PRODUCT).toString().contains("Fx")) {
        Option(FxQuote(dbObject.as[String](DbColumnNames.CCY2),
          dbObject.as[Integer](DbColumnNames.QUOTE_UNIT),
          dbObject.as[Double](DbColumnNames.PRICE),
          dbObject.as[String](DbColumnNames.TIMESTAMP),
          dbObject.as[String](DbColumnNames.SOURCE))(dbObject.as[String](DbColumnNames.CCY1)))
      } else {
        Option(PmQuote(dbObject.as[String](DbColumnNames.CCY2),
          dbObject.as[Integer](DbColumnNames.QUOTE_UNIT),
          dbObject.as[Double](DbColumnNames.PRICE),
          dbObject.as[String](DbColumnNames.TIMESTAMP),
          dbObject.as[String](DbColumnNames.SOURCE)))
      }
    }
  }

}