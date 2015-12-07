package hu.fxprices

import org.scalatest.WordSpecLike
import hu.fx.data.FxQuote
import hu.fx.data.Quote
import org.scalatest.Matchers
import hu.fx.data.PmQuote

class CurrencyInverterTest extends WordSpecLike with Matchers {

  val instruments = List(
    FxQuote("HUF", 1, 221, "", "YAHOO"),
    FxQuote("EUR", 1, 0.97, "", "YAHOO"))

  val expectedResult = List(
    FxQuote("USD", 1, 0.0045248869, "", "INVERTED_YAHOO")("HUF"),
    FxQuote("USD", 1, 1.0309278351, "", "INVERTED_YAHOO")("EUR"))

  "CurrencyInverter" should {
    val sut = CurrencyInverter()
    "create the same number of instruments if only Fx instruments are passed in" in {
      val inverse: List[Quote] = sut.createInverses(instruments)

      inverse.size should be(2)
    }

    "create the inverse prices for Fx instruments" in {
      val inverse: List[Quote] = sut.createInverses(instruments)

      expectedResult should equal(inverse)
    }

    "create the inverse prices for Fx instruments and skip Pm instruments" in {
      val instrumentswithPm = PmQuote("SILVER", 100, 11.876, "", "YAHOO") :: instruments ::: List(PmQuote("GOLD", 100, 11.876, "", "YAHOO"))
      val inverse: List[Quote] = sut.createInverses(instrumentswithPm)

      expectedResult should equal(inverse)
    }

  }
}