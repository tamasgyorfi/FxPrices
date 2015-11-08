package hu.fx.service.main

import hu.fx.service.api.Quote

abstract class Message

case object ApplicationStart extends Message
case object RequestQuote extends Message
case object QuoteRetrievalFailed extends Message
case object QuoteApiRequest
case class QuoteReply(quotes: List[Quote], senderName: String) extends Message
