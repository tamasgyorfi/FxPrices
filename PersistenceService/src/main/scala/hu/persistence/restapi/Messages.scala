package hu.persistence.restapi

import java.time.LocalDate
import hu.fx.data.Quote

class Message

case class CurrencyHistoryRequest(ccy1: String, ccy2: String, source: String, date: String) extends Message
case class CurrencyHistoryReply(quotes: List[Quote], errorMessage:String) extends Message

case class MaxPriceRequest(ccy1: String, ccy2: String, source: String, date:String) extends Message
case class MaxPriceReply(quote: Option[Quote], errorMessage: String) extends Message

case class MinPriceRequest(ccy1: String, ccy2: String, source: String, date:String) extends Message
case class MinPriceReply(quote: Option[Quote], errorMessage: String) extends Message

case class MeanPriceRequest(ccy1: String, ccy2: String, source: String, date:String) extends Message
case class MeanPriceReply(quote: Option[Quote], errorMessage: String) extends Message