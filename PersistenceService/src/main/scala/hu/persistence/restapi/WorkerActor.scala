package hu.persistence.restapi

import akka.actor.Actor
import hu.persistence.api.DataExtractor
import java.time.LocalDate

class WorkerActor(dataExtractor: DataExtractor) extends Actor {

  def receive(): Receive = {
    case CurrencyHistoryRequest(ccy1, ccy2, date) => {
      try {
        val localDate = LocalDate.parse(date)
        sender ! CurrencyHistoryReply(dataExtractor.getCurrencyPairHistory(ccy1, ccy2, localDate), "")
      } catch {
        case ex: Exception => sender ! CurrencyHistoryReply(List(), s"Error: Unable to parse date ${date}")
      }
    }
  }
}