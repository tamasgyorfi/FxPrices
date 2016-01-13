package hu.persistence.restapi

import java.time.LocalDate
import hu.fx.data.Quote

class Message

case class CurrencyHistoryRequest(ccy1: String, ccy2: String, date: String) extends Message
case class CurrencyHistoryReply(quotes: List[Quote], errorMessage:String) extends Message
