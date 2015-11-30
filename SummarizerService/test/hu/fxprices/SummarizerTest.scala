package hu.fxprices

import org.scalatest.BeforeAndAfter
import org.scalatest.WordSpecLike

import hu.fx.data.FxQuote
import hu.fx.data.Quote

class SummarizerTest extends WordSpecLike {

  val sut = Summarizer()

  val hufQuote = FxQuote("HUF", 1, 33.11, "", "YAHOO")
  val oldLekQuote = FxQuote("LEK", 1, 3.11, "", "YAHOO")
  val newLekQuote = FxQuote("LEK", 1, 99.11, "", "YAHOO")
  val arpQuote = FxQuote("ARP", 1, 133.11, "", "YAHOO")

  val chfQuote = FxQuote("CHF", 1, 1.3, "", "APILAYER")
  val rubQuote = FxQuote("RUB", 1, 3.3, "", "APILAYER")
  val inrQuote = FxQuote("INR", 1, 12.9, "", "APILAYER")

  "Summarizer" should {
    "not update those prices that have changed" in {
      val firstYahooQuotes = List(hufQuote, oldLekQuote, arpQuote)
      val secondYahooQuotes = List(hufQuote, newLekQuote, arpQuote)

      val apilayerQuotes = List(chfQuote, rubQuote, inrQuote)

      sut.update("YAHOO", firstYahooQuotes)
      sut.update("APILAYER", apilayerQuotes)
      sut.update("YAHOO", secondYahooQuotes)
      sut.update("APILAYER", apilayerQuotes)

      val currentYahooPrices = sut.getPricesFor("YAHOO")
      val currentApilayerPrices = sut.getPricesFor("APILAYER")

      assert(currentYahooPrices.contains(hufQuote))
      assert(currentYahooPrices.contains(FxQuote("LEK", 1, 99.11, "", "YAHOO")))
      assert(currentYahooPrices.contains(arpQuote))

      assert(currentApilayerPrices.contains(chfQuote))
      assert(currentApilayerPrices.contains(rubQuote))
      assert(currentApilayerPrices.contains(inrQuote))

    }
  }
}