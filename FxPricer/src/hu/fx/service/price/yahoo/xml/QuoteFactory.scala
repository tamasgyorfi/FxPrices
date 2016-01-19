package hu.fx.service.price.yahoo.xml

import scala.xml.Node

import hu.fx.data.EmptyQuote
import hu.fx.data.FxQuote
import hu.fx.data.Quote
import hu.fx.service.price.yahoo.config.YahooPriceSource

class QuoteFactory extends YahooPriceSource {

  def fromNode(quote: Node): Quote = {
    val name = getAttribute(quote, NAME_ATTRIBUTE)
    if (name.isEmpty) EmptyQuote

    createFxQuote(quote, name)
  }

  private def createFxQuote(node: Node, currencyName: String): Quote = {

      FxQuote(
        getQuoteName(currencyName),
        getAttribute(node, VOLUME_ATTRIBUTE) toInt,
        getAttribute(node, PRICE_ATTRIBUTE) toDouble,
        getAttribute(node, TIMESTAMP_ATTRIBUTE),
        PROVIDER)
  }

  private def getQuoteName(currencyName: String) = {
    currencyName.indexOf("/") match {
      case x if x < 0 => currencyName
      case _          => currencyName.split("/")(1)
    }
  }

  private def getAttribute(quote: Node, attribute: String): String = {
    (quote \\ "field").filter { node => (node \ ("@" + NAME_ATTRIBUTE)).text equals attribute } text
  }
}

object QuoteFactory {
  def createFromNode(node: Node): Quote = {
    new QuoteFactory().fromNode(node)
  }
}