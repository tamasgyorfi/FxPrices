package hu.fx.service.price.apilayer

import java.util.Date
import org.scalatest.FlatSpec
import org.scalatest.Matchers
import hu.fx.service.dates.DateComponent
import hu.fx.data.FxQuote
import hu.fx.service.price.apilayer.json.QuoteJsonParser

object TestDateComponent extends DateComponent {
  def currentDate = new Date(2015, 10, 24)
  def dateFormatted(date: Date) = "2015-10-4T7:24:36+0000"
}

trait TestData {
  val testJson = "{\r\n  \"success\":true,\r\n  \"terms\":\"https:\\/\\/currencylayer.com\\/terms\",\r\n  \"privacy\":\"https:\\/\\/currencylayer.com\\/privacy\",\r\n  \"timestamp\":1445678893,\r\n  \"source\":\"USD\",\r\n  \"quotes\":{\r\n    \"USDAED\":3.67255,\r\n    \"USDAFN\":64.519997\r\n  }\r\n}"
  val sut = new QuoteJsonParser(TestDateComponent)
}

class QuoteJsonParserTest extends FlatSpec with Matchers with TestData {

  "JSON parser" should "parse JSON string for two fx quotes" in {
    val result = sut.parseJsonForQuotes(testJson)

    result.size should be(2)
    result(0) shouldBe an[FxQuote]
    result(0).ccy1 should be("USD")
    result(0).ccy2 should be("AED")
    result(0).price should be(3.67255)
    result(0).source should be("APILAYER")
    result(0).timestamp should be("2015-10-4T7:24:36+0000")

    result(1) shouldBe an[FxQuote]
    result(1).ccy1 should be("USD")
    result(1).ccy2 should be("AFN")
    result(1).price should be(64.519997)
    result(1).source should be("APILAYER")
    result(1).timestamp should be("2015-10-4T7:24:36+0000")

  }

  it should "skip an erroneous quote and still parse the other" in {
    val jsonWithError = testJson.replace("3.67255", "\""+"ERROR!" + "\"")
    val result = sut.parseJsonForQuotes(jsonWithError)

    result.size should be(1)

    result(0) shouldBe an[FxQuote]
    result(0).ccy1 should be("USD")
    result(0).ccy2 should be("AFN")
    result(0).price should be(64.519997)
    result(0).source should be("APILAYER")

  }
}