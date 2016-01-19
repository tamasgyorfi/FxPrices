package hu.persistence.restapi

import java.time.LocalDate
import hu.fx.data.Quote
import hu.persistence.api.QuoteComparison

class Message

case class CurrencyHistoryRequest(ccy1: String, ccy2: String, source: String, date: String) extends Message
case class CurrencyHistoryRequestRange(ccy1: String, ccy2: String, source: String, from: String, to:String) extends Message
case class CurrencyHistoryReply(quotes: List[Quote], errorMessage:String) extends Message

case class MaxPriceRequest(ccy1: String, ccy2: String, source: String, date:String) extends Message
case class MinPriceRequest(ccy1: String, ccy2: String, source: String, date:String) extends Message
case class MeanPriceRequest(ccy1: String, ccy2: String, source: String, date:String) extends Message
case class PriceReply(quote: Option[Quote], errorMessage: String) extends Message

case class ComparisonRequest(firstCcy1:String, firstCcy2:String, secondCcy1:String, secondCcy2:String, source:String, date:String) extends Message
case class ComparisonReply(comparison: Option[QuoteComparison], errorMessage: String) extends Message