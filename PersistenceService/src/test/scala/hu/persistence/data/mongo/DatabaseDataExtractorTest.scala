package hu.persistence.data.mongo

import org.scalatest.BeforeAndAfterAll
import org.scalatest.Matchers
import org.scalatest.FlatSpec
import org.scalatest.FunSuite
import org.scalatest.FlatSpecLike
import com.github.fakemongo.Fongo
import scala.io.Source
import com.mongodb.util.JSON
import com.mongodb.DBObject
import com.mongodb.casbah.MongoCollection
import java.time.LocalDate
import hu.fx.data.SimpleQuote
import hu.fx.data.FxQuote
import hu.fx.data.EmptyQuote

class DatabaseDataExtractorTest extends FunSuite with BeforeAndAfterAll with FongoFixture{

  val sut = new DatabaseDataExtractor(mongoCollection)

  override def beforeAll() = {
    val dbLines = Source.fromFile("src/test/resources/database.txt").getLines()
    dbLines.map { dbLine => JSON.parse(dbLine).asInstanceOf[DBObject] }.foreach { mongoCollection.insert(_) }
  }

  test("the test database should be up and running") {
    assert(100 === mongoCollection.count())
  }

  test("DatabaseDataExtractor -getHighestPrice- should be able to select the a currency pair with highest value on a date") {
    val quote = sut.getHighestPrice("USD", "KRW", LocalDate.of(2016, 1, 11))
    val expectedQuote = FxQuote("KRW", 0, 22450.0, "2016-01-11T15:07:04+0000", "YAHOO")("USD")

    assert(expectedQuote === quote.getOrElse(EmptyQuote))
  }

  test("DatabaseDataExtractor -getHighestPrice- should return empty, when we have no highest price on a certain date ") {
    val quote = sut.getHighestPrice("USD", "KRW", LocalDate.of(2011, 1, 11))
    val expectedQuote = Option.empty

    assert(expectedQuote === quote)
  }

  test("DatabaseDataExtractor -getHighestPrice- should return empty, when the currency pair is unknown ") {
    val quote = sut.getHighestPrice("USD", "UNKNOWN", LocalDate.of(2011, 1, 11))
    val expectedQuote = Option.empty

    assert(expectedQuote === quote)
  }

  test("DatabaseDataExtractor -getCurrencyPairHistory- should return all quotes on a specific date") {
    val quotes = sut.getCurrencyPairHistory("USD", "KRW", LocalDate.of(2016, 1, 11))

    assert(8 == quotes.size)
    val expectedResult = List(
      FxQuote("KRW", 0, 1204.14502, "2016-01-11T15:11:09+0000", "YAHOO"),
      FxQuote("KRW", 0, 6.91, "2016-01-11T15:07:03+0000", "YAHOO"),
      FxQuote("KRW", 0, 22450.0, "2016-01-11T15:07:04+0000", "YAHOO"),
      FxQuote("KRW", 0, 7.99125, "2016-01-11T15:07:10+0000", "YAHOO"),
      FxQuote("KRW", 0, 78.408951, "2016-01-11T15:07:02+0000", "YAHOO"),
      FxQuote("KRW", 0, 20.385, "2016-01-11T15:07:01+0000", "YAHOO"),
      FxQuote("KRW", 0, 6.35, "2016-01-11T15:09:00+0000", "YAHOO"),
      FxQuote("KRW", 0, 2.425, "2016-01-11T15:07:00+0000", "YAHOO"))

    quotes.map(expectedResult.contains(_))
  }

  test("DatabaseDataExtractor -getCurrencyPairHistory- should return empty, when we have no highest price on a certain date") {
    val quotes = sut.getCurrencyPairHistory("USD", "KRW", LocalDate.of(2011, 1, 11))

    assert(0 == quotes.size)
  }

  test("DatabaseDataExtractor -getCurrencyPairHistory- should return empty, the currency pair is unknown") {
    val quotes = sut.getCurrencyPairHistory("UNKNOWN", "KRW", LocalDate.of(2011, 1, 11))

    assert(0 == quotes.size)
  }
}