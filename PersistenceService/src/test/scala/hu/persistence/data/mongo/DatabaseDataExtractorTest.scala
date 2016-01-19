package hu.persistence.data.mongo

import java.time.LocalDate

import org.scalatest.BeforeAndAfterAll
import org.scalatest.WordSpecLike

import hu.fx.data.EmptyQuote
import hu.fx.data.FxQuote
import hu.persistence.FongoFixture
import hu.persistence.TestData

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
      assert(187 === mongoCollection.count())
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
      val quote = sut.getHighestPrice("USD", "UNKNOWN", "YAHOO", LocalDate.of(2016, 1, 11))
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
      val quote = sut.getLowestPrice("USD", "UNKNOWN", "YAHOO", LocalDate.of(2016, 1, 11))
      val expectedQuote = Option.empty

      assert(expectedQuote === quote)
    }
  }

  "DatabaseDataExtractor -getCurrencyPairHistory- for one date USD/KRW" should {
    "return all quotes on a specific date - YAHOO source" in {
      val quotes = sut.getCurrencyPairHistory("USD", "KRW", "YAHOO", LocalDate.of(2016, 1, 11))

      assert(allUsdKrwYahoo.size === quotes.size)
      assert(true === quotes.forall(allUsdKrwYahoo.contains(_)))
    }

    "return all quotes on a specific date - YAHOO source USD/CAD" in {
      val quotes = sut.getCurrencyPairHistory("USD", "CAD", "YAHOO", LocalDate.of(2016, 1, 11))

      assert(usdCadYahooOnCertainDate.size === quotes.size)
      assert(true === quotes.forall(usdCadYahooOnCertainDate.contains(_)))
    }

    "return all quotes on a specific date - ApiLayer source" in {
      val quotes = sut.getCurrencyPairHistory("USD", "KRW", "APILAYER", LocalDate.of(2016, 1, 11))

      assert(allUsdKrwApiLayer.size == quotes.size)
      assert(true === quotes.forall(allUsdKrwApiLayer.contains(_)))
    }

    "return empty, when we have no highest price on a certain date" in {
      val quotes = sut.getCurrencyPairHistory("USD", "KRW", "YAHOO", LocalDate.of(2011, 1, 11))
      assert(0 == quotes.size)
    }

    "return empty, the currency pair is unknown" in {
      val quotes = sut.getCurrencyPairHistory("UNKNOWN", "KRW", "YAHOO", LocalDate.of(2016, 1, 11))
      assert(0 == quotes.size)
    }
  }

  "DatabaseDataExtractor -getCurrencyPairHistory- for two dates" should {
    "return all quotes in a specific date range- YAHOO source" in {
      val quotes = sut.getCurrencyPairHistory("USD", "CAD", "YAHOO", LocalDate.of(2016, 1, 11), LocalDate.of(2016, 1, 16))

      assert(6 == quotes.size)
      assert(true === quotes.forall(allUsdCadYahoo.contains(_)))
    }

    "return empty, when we have no highest price between certain dates" in {
      val quotes = sut.getCurrencyPairHistory("USD", "KRW", "YAHOO", LocalDate.of(2011, 1, 11), LocalDate.of(2011, 1, 15))
      assert(0 == quotes.size)
    }

    "return empty, the currency pair is unknown" in {
      val quotes = sut.getCurrencyPairHistory("UNKNOWN", "KRW", "YAHOO", LocalDate.of(2016, 1, 11), LocalDate.of(2016, 1, 15))
      assert(0 == quotes.size)
    }
  }

  "DatabaseDataExtractor -getDailyMean-" should {
    "return mean on a specific date - YAHOO source" in {
      val quotes = sut.getDailyMean("USD", "KRW", "YAHOO", LocalDate.of(2016, 1, 11))

      assert(FxQuote("KRW", 0, 2972.0769026249995, "2016-01-11", "YAHOO") === quotes.getOrElse(EmptyQuote))
    }

    "return mean on a specific date - ApiLayer source" in {
      val quote = sut.getDailyMean("USD", "KRW", "APILAYER", LocalDate.of(2016, 1, 11))

      assert(FxQuote("KRW", 1, 1204.234985, "2016-01-11", "APILAYER") === quote.get)
    }

    "return empty, when we have no highest price on a certain date" in {
      val quotes = sut.getDailyMean("USD", "KRW", "YAHOO", LocalDate.of(2011, 1, 11))
      assert(0 == quotes.size)
    }

    "return empty, the currency pair is unknown" in {
      val quotes = sut.getDailyMean("UNKNOWN", "KRW", "YAHOO", LocalDate.of(2016, 1, 11))
      assert(0 == quotes.size)
    }
  }

  "DatabaseDataExtractor -compare-" should {
    "return comparison on a specific date - YAHOO source" in {
      val quotes = sut.compare("USD", "KRW", "USD", "CAD", "YAHOO", LocalDate.of(2016, 1, 11))

      assert(allUsdKrwYahoo.size === quotes.get.quote1.size)
      assert(quotes.get.quote1.forall(allUsdKrwYahoo.contains(_)))

      assert(usdCadYahooOnCertainDate.size === quotes.get.quote2.size)
      assert(quotes.get.quote2.forall(usdCadYahooOnCertainDate.contains(_)))
    }

    "return empty comparison, when we have no highest price on a certain date" in {
      val quotes = sut.compare("USD", "KRW", "USD", "CAD", "YAHOO", LocalDate.of(2011, 1, 11))
      assert(None === quotes)
    }

    "return empty comparison, when source is unknown" in {
      val quotes = sut.compare("USD", "KRW", "USD", "CAD", "YAHOOOOOO", LocalDate.of(2016, 1, 11))
      assert(None === quotes)
    }

    "return the known currency's history, when the first currency pair is unknown" in {
      val quotes = sut.compare("UNKNOWN", "KRW", "USD", "CAD", "YAHOO", LocalDate.of(2016, 1, 11))

      assert(Nil === quotes.get.quote1)

      assert(usdCadYahooOnCertainDate.size === quotes.get.quote2.size)
      assert(quotes.get.quote2.forall(usdCadYahooOnCertainDate.contains(_)))
    }

    "return the known currency's history, when the second currency pair is unknown" in {
      val quotes = sut.compare("USD", "KRW", "UNKNOWN", "CAD", "YAHOO", LocalDate.of(2016, 1, 11))

      assert(allUsdKrwYahoo.size === quotes.get.quote1.size)
      assert(quotes.get.quote1.forall(allUsdKrwYahoo.contains(_)))

      assert(Nil === quotes.get.quote2)
    }
  }
}