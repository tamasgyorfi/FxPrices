package hu.fx.service.pricing

import hu.fx.data.Quote

abstract class Message

case object ApplicationStart extends Message
case object RequestQuote extends Message
case class QuotesRefresh(quotes: List[Quote], senderName: String) extends Message
case object QuoteRetrievalFailed extends Message

case object AllQuotesApiRequest extends Message
case class AllQuotesApiReply(quotes: Map[String, List[Quote]], errorMessage: String)extends Message

case class QuoteApiRequest(currency: String, counter: String) extends Message
case class QuoteApiReply(quotes: List[Quote], errorMessage: String)extends Message
