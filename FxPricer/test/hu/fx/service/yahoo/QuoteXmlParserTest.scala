package hu.fx.service.yahoo

import org.scalatest.FlatSpec
import org.scalatest.Matchers
import hu.fx.service.api.FxQuote
import hu.fx.service.api.FxQuote
import hu.fx.service.api.PmQuote

trait TestData {
  val header = "<list version=\"1.0\"><meta><type>resource-list</type></meta><resources start=\"0\" count=\"173\">"
  val fxQuote = "<resource classname=\"Quote\"><field name=\"name\">USD/KRW</field><field name=\"price\">1.9</field><field name=\"symbol\">KRW=X</field><field name=\"ts\">1445531793</field><field name=\"type\">currency</field><field name=\"utctime\">2015-10-22T16:36:33+0000</field><field name=\"volume\">0</field></resource>"
  val pmQuote = "<resource classname=\"Quote\"><field name=\"name\">SILVER 1 OZ 999 NY</field><field name=\"price\">0.063789</field><field name=\"symbol\">KRW=X</field><field name=\"ts\">1445531793</field><field name=\"type\">currency</field><field name=\"utctime\">2015-10-22T16:36:33+0000</field><field name=\"volume\">100</field></resource>"
  val tailer = "</resources></list>"

  val sut = new QuoteXmlParser
}

class QuoteXmlParserTest extends FlatSpec with Matchers with TestData {

  "The XML parser" should "parse an XML with a single fx block" in {
    val xmlWithOneQuote = header + fxQuote + tailer
    val result = sut.parse(xmlWithOneQuote)

    result.size should be(1)
    val resultQuote = result(0)

    resultQuote shouldBe an[FxQuote]
    resultQuote.ccy2 should be("KRW")
    resultQuote.ccy1 should be("USD")
    resultQuote.quoteUnit should be(0)
    resultQuote.price should be(1.9)
    resultQuote.timestamp should be("2015-10-22T16:36:33+0000")
  }

  it should "parse an XML with a single pm block" in {
    val xmlWithOneQuote = header + pmQuote + tailer
    val result = sut.parse(xmlWithOneQuote)

    result.size should be(1)
    val resultQuote = result(0)

    resultQuote shouldBe an[PmQuote]
    resultQuote.ccy2 should be("SILVER 1 OZ 999 NY")
    resultQuote.ccy1 should be("USD")
    resultQuote.quoteUnit should be(100)
    resultQuote.price should be(0.063789)
    resultQuote.timestamp should be("2015-10-22T16:36:33+0000")
  }

  it should "parse an XML with a single pm block and an fx block" in {
    val xmlWithTwoQuotes = header + pmQuote + fxQuote + tailer
    val result = sut.parse(xmlWithTwoQuotes)

    result.size should be(2)

    val resultPmQuote = result(0)
    resultPmQuote shouldBe an[PmQuote]
    resultPmQuote.ccy2 should be("SILVER 1 OZ 999 NY")
    resultPmQuote.ccy1 should be("USD")
    resultPmQuote.quoteUnit should be(100)
    resultPmQuote.price should be(0.063789)
    resultPmQuote.timestamp should be("2015-10-22T16:36:33+0000")

    val resultFxQuote = result(1)
    resultFxQuote shouldBe an[FxQuote]
    resultFxQuote.ccy2 should be("KRW")
    resultFxQuote.ccy1 should be("USD")
    resultFxQuote.quoteUnit should be(0)
    resultFxQuote.price should be(1.9)
    resultFxQuote.timestamp should be("2015-10-22T16:36:33+0000")

  }

  it should "parse an XML with an erroneous fx block and correct pm block" in {
    val erroneousFx = fxQuote.replace("1.9", "Error")
    val xmlWithTwoQuotes = header + erroneousFx + pmQuote + tailer
    val result = sut.parse(xmlWithTwoQuotes)

    result.size should be(1)

    val resultPmQuote = result(0)
    resultPmQuote shouldBe an[PmQuote]
    resultPmQuote.ccy2 should be("SILVER 1 OZ 999 NY")
    resultPmQuote.ccy1 should be("USD")
    resultPmQuote.quoteUnit should be(100)
    resultPmQuote.price should be(0.063789)
    resultPmQuote.timestamp should be("2015-10-22T16:36:33+0000")
  }

}