package hu.fxprices

import hu.fx.data.Quote
import scala.collection.parallel.mutable.ParMap

class Summarizer {

  private val ROUNDING_SCALE = 10
  private val ROUNDING_MODE = scala.math.BigDecimal.RoundingMode.HALF_EVEN
  private val averagedQuotes = ParMap[String, List[Quote]]()

  def update(provider: String, quotes: List[Quote]): Unit = {
    averagedQuotes putOrReplace (provider, quotes, avgQuotes(averagedQuotes get(provider) get, quotes))
  }

  def getPricesFor(provider: String) = {
    averagedQuotes get(provider) get
  }

  private def avgQuotes(oldQuotes: List[Quote], freshQuotes: List[Quote]): List[Quote] = {
    for {
      freshQuote <- freshQuotes

      val updated = oldQuotes indexOf (freshQuote) match {
        case x if x > -1 => freshQuote withNewPricePrice(averagePrices(oldQuotes(x), freshQuote))
        case _           => freshQuote
      }
    } yield (updated)
  }

  private def averagePrices(oldQuote: Quote, freshQuote: Quote): Double = {
    val updatedPrice: BigDecimal = (oldQuote.price + freshQuote.price) / 2
    updatedPrice setScale (ROUNDING_SCALE, ROUNDING_MODE) toDouble
  }

  implicit class ParMapImprovements[A, B](map: ParMap[A, B]) {
    def putOrReplace(key: A, unDefinedCase: => B, definedCase: => B) = {
      map get key match {
        case None        => map + (key, unDefinedCase)
        case Some(value) => map updated (key, definedCase)
      }
    }
  }
}

object Summarizer {
  def apply() = {
    new Summarizer()
  }
}