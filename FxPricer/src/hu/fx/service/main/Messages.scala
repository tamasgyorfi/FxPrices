package hu.fx.service.main

import hu.fx.data.Quote

abstract class Message

case object ApplicationStart extends Message
case object RequestQuote extends Message
case class QuotesRefresh(quotes: List[Quote], senderName: String) extends Message
case object QuoteRetrievalFailed extends Message

case object AllQuotesApiRequest
case class AllQuotesApiReply(quotes: Map[String, List[Quote]])

case class QuoteApiRequest(counter: String) extends Message
case class QuoteApiReply(quotes: List[Quote]) extends Message
