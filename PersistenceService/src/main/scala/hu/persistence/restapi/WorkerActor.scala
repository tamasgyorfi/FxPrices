package hu.persistence.restapi

import akka.actor.Actor
import hu.persistence.api.DataExtractor
import java.time.LocalDate

class WorkerActor(dataExtractor: DataExtractor) extends Actor {

  def receive(): Receive = {
    case CurrencyHistoryRequest(ccy1, ccy2, source, date) => {
      try {
        val localDate = LocalDate.parse(date)
        sender ! CurrencyHistoryReply(dataExtractor.getCurrencyPairHistory(ccy1, ccy2, source, localDate), "")
      } catch {
        case ex: Exception => sender ! CurrencyHistoryReply(List(), s"Error: Unable to parse request for ${ccy1}, ${ccy2} and ${date}")
      }
    }

    case MaxPriceRequest(ccy1, ccy2, source, date) => {
      try {
        val localDate = LocalDate.parse(date)
        val result = MaxPriceReply(dataExtractor.getHighestPrice(ccy1, ccy2, source, localDate), "")
        sender ! result
      } catch {
        case ex: Exception => sender ! MaxPriceReply(Option.empty, s"Error: Unable to parse request for ${ccy1}, ${ccy2} and ${date}")
      }
    }

    case MinPriceRequest(ccy1, ccy2, source, date) => {
      try {
        val localDate = LocalDate.parse(date)
        val result = MinPriceReply(dataExtractor.getLowestPrice(ccy1, ccy2, source, localDate), "")
        sender ! result
      } catch {
        case ex: Exception => sender ! MinPriceReply(Option.empty, s"Error: Unable to parse request for ${ccy1}, ${ccy2} and ${date}")
      }
    }

  }
}