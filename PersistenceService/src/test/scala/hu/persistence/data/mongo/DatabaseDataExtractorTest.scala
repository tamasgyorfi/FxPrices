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
import org.scalatest.fixture.FlatSpecLike
import org.scalatest.WordSpecLike

class DatabaseDataExtractorTest extends WordSpecLike with BeforeAndAfterAll with FongoFixture with TestData {

  val sut = new DatabaseDataExtractor(mongoCollection)

  override def beforeAll() = {
    insertTestData()
  }

  override def afterAll() {
    clearTestData()
  }

  "the test database " should {
    "be up and running" in {
      assert(180 === mongoCollection.count())
    }
  }

  "DatabaseDataExtractor -getHighestPrice-" should {
    "be able to select the a currency pair with highest value on a date - for YAHOO source" in {
      val quote = sut.getHighestPrice("USD", "KRW", "YAHOO", LocalDate.of(2016, 1, 11))
      val expectedQuote = FxQuote("KRW", 0, 22450.0, "2016-01-11T15:07:04+0000", "YAHOO")("USD")

      assert(expectedQuote === quote.getOrElse(EmptyQuote))
    }

    "be able to select the a currency pair with highest value on a date - for APILAYER source" in {
      val quote = sut.getHighestPrice("USD", "KRW", "APILAYER", LocalDate.of(2016, 1, 11))
      val expectedQuote = allUsdKrwApiLayer(0)

      assert(expectedQuote === quote.getOrElse(EmptyQuote))
    }

    "return empty, when we have no highest price on a certain date " in {
      val quote = sut.getHighestPrice("USD", "KRW", "YAHOO", LocalDate.of(2011, 1, 11))
      val expectedQuote = Option.empty

      assert(expectedQuote === quote)
    }

    "return empty, when the currency pair is unknown " in {
      val quote = sut.getHighestPrice("USD", "UNKNOWN", "YAHOO", LocalDate.of(2011, 1, 11))
      val expectedQuote = Option.empty

      assert(expectedQuote === quote)
    }
  }

  "DatabaseDataExtractor -getLowestPrice- " should {
    "be able to select the a currency pair with lowest value on a date - for YAHOO source" in {
      val quote = sut.getLowestPrice("USD", "KRW", "YAHOO", LocalDate.of(2016, 1, 11))
      val expectedQuote = FxQuote("KRW", 0, 2.425, "2016-01-11T15:07:00+0000", "YAHOO")

      assert(expectedQuote === quote.getOrElse(EmptyQuote))
    }

    "be able to select the a currency pair with lowest value on a date - for APILAYER source" in {
      val quote = sut.getLowestPrice("USD", "KRW", "APILAYER", LocalDate.of(2016, 1, 11))
      val expectedQuote = allUsdKrwApiLayer(0)

      assert(expectedQuote === quote.getOrElse(EmptyQuote))
    }

    "return empty, when we have no lowest price on a certain date " in {
      val quote = sut.getLowestPrice("USD", "KRW", "YAHOO", LocalDate.of(2011, 1, 11))
      val expectedQuote = Option.empty

      assert(expectedQuote === quote)
    }

    "return empty, when the currency pair is unknown " in {
      val quote = sut.getLowestPrice("USD", "UNKNOWN", "YAHOO", LocalDate.of(2011, 1, 11))
      val expectedQuote = Option.empty

      assert(expectedQuote === quote)
    }
  }

  "DatabaseDataExtractor -getCurrencyPairHistory-" should {
    "return all quotes on a specific date - YAHOO source" in {
      val quotes = sut.getCurrencyPairHistory("USD", "KRW", "YAHOO", LocalDate.of(2016, 1, 11))

      assert(8 == quotes.size)
      quotes.map(allUsdKrwYahoo.contains(_))
    }

    "return all quotes on a specific date - ApiLayer source" in {
      val quotes = sut.getCurrencyPairHistory("USD", "KRW", "APILAYER", LocalDate.of(2016, 1, 11))

      assert(1 == quotes.size)
      quotes.map(allUsdKrwApiLayer.contains(_))
    }

    "return empty, when we have no highest price on a certain date" in {
      val quotes = sut.getCurrencyPairHistory("USD", "KRW", "YAHOO", LocalDate.of(2011, 1, 11))
      assert(0 == quotes.size)
    }

    "return empty, the currency pair is unknown" in {
      val quotes = sut.getCurrencyPairHistory("UNKNOWN", "KRW", "YAHOO", LocalDate.of(2011, 1, 11))
      assert(0 == quotes.size)
    }
  }

  "DatabaseDataExtractor -getDailyMean-" should {
    "return mean on a specific date - YAHOO source" in {
      val quotes = sut.getDailyMean("USD", "KRW", "YAHOO", LocalDate.of(2016, 1, 11))

      assert(FxQuote("KRW", 0, 2972.0769026249995, "2016-01-11", "YAHOO") === quotes.getOrElse(EmptyQuote))
    }

    "return mean on a specific date - ApiLayer source" in {
      val quotes = sut.getDailyMean("USD", "KRW", "APILAYER", LocalDate.of(2016, 1, 11))

      assert(1 == quotes.size)
      quotes.map(allUsdKrwApiLayer.contains(_))
    }

    "return empty, when we have no highest price on a certain date" in {
      val quotes = sut.getDailyMean("USD", "KRW", "YAHOO", LocalDate.of(2011, 1, 11))
      assert(0 == quotes.size)
    }

    "return empty, the currency pair is unknown" in {
      val quotes = sut.getDailyMean("UNKNOWN", "KRW", "YAHOO", LocalDate.of(2011, 1, 11))
      assert(0 == quotes.size)
    }
  }
}