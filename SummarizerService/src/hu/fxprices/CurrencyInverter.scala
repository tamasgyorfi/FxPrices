package hu.fxprices

import hu.fx.data.Quote
import hu.fx.data.FxQuote
import java.math.RoundingMode

class CurrencyInverter {

  def createInverses(currencies: List[Quote]) = {
    currencies filter {
      case FxQuote(_, _, _, _, _) => true
      case _                      => false
    } map {
      currency => FxQuote(currency.ccy1, currency.quoteUnit, invertAndRound(currency.price), currency.timestamp, "INVERTED_" + currency.source)(currency.ccy2)
    }
  }

  private def invertAndRound(price: Double): Double = {
    BigDecimal.decimal(1 / price).setScale(ROUNDING_SCALE, ROUNDING_MODE).toDouble
  }
}

object CurrencyInverter {
  def apply() = {
    new CurrencyInverter()
  }
}