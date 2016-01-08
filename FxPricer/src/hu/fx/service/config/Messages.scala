package hu.fx.service.config

import hu.fx.data.Quote

abstract class Message
abstract class ReplyMessage[A] extends Message {
  def getResult(): A
}

case object ApplicationStart extends Message
case object RequestQuote extends Message
case class QuotesRefresh(quotes: List[Quote], senderName: String) extends Message
case object QuoteRetrievalFailed extends Message

case object AllQuotesApiRequest
case class AllQuotesApiReply(quotes: Map[String, List[Quote]]) extends ReplyMessage[Map[String, List[Quote]]] {
  def getResult() = {
    quotes
  }
}

case class QuoteApiRequest(currency: String, counter: String) extends Message
case class QuoteApiReply(quotes: List[Quote]) extends ReplyMessage[List[Quote]] {
  def getResult() = {
    quotes
  }
}
