package hu.fx.service.providers.yahoo.xml

import scala.xml.Node
import scala.xml.XML
import hu.fx.service.api.Quote
import hu.fx.service.providers.yahoo.config.YahooPriceSource

class QuoteXmlParser extends YahooPriceSource {
  def parse(source: String): List[Quote] = {
    val nodeSequence = XML.loadString(source)
    val rates = nodeSequence \\ YAHOO_RATES_IDENTIFIER

    rates map { node => xmlNodeToRate(node) } filter { x => x.isDefined } map { x => x.get } toList
  }

  private def xmlNodeToRate(node: Node): Option[Quote] = {
    try {
      Some(QuoteFactory.createFromNode(node))
    } catch {
      case e: Exception => None
    }
  }
}

object QuoteXmlParser {
  def apply() = new QuoteXmlParser
}
