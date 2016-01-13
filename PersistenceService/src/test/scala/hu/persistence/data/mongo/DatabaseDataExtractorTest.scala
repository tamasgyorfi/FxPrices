package hu.persistence.data.mongo

import java.time.LocalDate
import scala.io.Source
import org.scalatest.BeforeAndAfterAll
import org.scalatest.FunSuite
import com.mongodb.DBObject
import com.mongodb.util.JSON
import hu.fx.data.EmptyQuote
import hu.fx.data.FxQuote
import hu.persistence.FongoFixture
import hu.persistence.TestData

class DatabaseDataExtractorTest extends FunSuite with BeforeAndAfterAll with FongoFixture with TestData {

  val sut = new DatabaseDataExtractor(mongoCollection)

  override def beforeAll() = {
    insertTestData()
  }

  override def afterAll() {
    clearTestData()
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

    quotes.map(allUsdKrw.contains(_))
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