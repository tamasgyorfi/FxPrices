package hu.fx.service.providers.apilayer.json

import hu.fx.service.api.FxQuote
import hu.fx.service.api.Quote
import hu.fx.service.dates.DateComponent
import net.liftweb.json.JsonAST.JValue
import net.liftweb.json.JsonParser
import hu.fx.service.providers.apilayer.config.ApiLayerPriceSource

class QuoteJsonParser(dateComponent: DateComponent) extends ApiLayerPriceSource {
  def parseJsonForQuotes(source: String): List[Quote] = {
    val parsedJson = JsonParser.parse(source)
    val quotes = (parsedJson \ QUOTE_KEY).children

    quotes map (jsonQuote => jsonQuoteAsQuoteObject(jsonQuote)) filter (x => x.isDefined) map (x => x.get)
  }

  private def jsonQuoteAsQuoteObject(jsonQuote: JValue): Option[Quote] = {
    try {
      val asTouple = jsonQuote.values.asInstanceOf[(String, Double)]
      val ccy2 = asTouple._1 substring (3, 6)
      val price = asTouple._2
      val dateTime = dateComponent.dateFormatted(dateComponent.currentDate())

      Some(FxQuote(ccy2, 1, price, dateTime, PROVIDER))
    } catch {
      case e: Exception => None
    }
  }
}

object QuoteJsonParser  {
  def apply(dateComponent : DateComponent) = new QuoteJsonParser(dateComponent)
}