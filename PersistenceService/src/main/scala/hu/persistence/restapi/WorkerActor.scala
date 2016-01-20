package hu.persistence.restapi

import java.time.LocalDate
import akka.actor.Actor
import akka.actor.actorRef2Scala
import hu.persistence.api.DataExtractor
import hu.persistence.api.QuoteComparison
import hu.persistence.api.QuoteComparison

class WorkerActor(dataExtractor: DataExtractor) extends Actor {

  implicit def stringToDate(date: String) = {
    LocalDate.parse(date)
  }

  def receive(): Receive = {
    case CurrencyHistoryRequest(ccy1, ccy2, source, date) => {
      try {
        sender ! CurrencyHistoryReply(dataExtractor.getCurrencyPairHistory(ccy1, ccy2, source, date), "")
      } catch {
        case ex: Exception => sender ! CurrencyHistoryReply(List(), s"Error: Unable to parse request for ${ccy1}, ${ccy2} and ${date}")
      }
    }

    case CurrencyHistoryRequestRange(ccy1, ccy2, source, from, to) => {
      try {
        sender ! CurrencyHistoryReply(dataExtractor.getCurrencyPairHistory(ccy1, ccy2, source, from, to), "")
      } catch {
        case ex: Exception => sender ! CurrencyHistoryReply(List(), s"Error: Unable to parse request for ${ccy1}, ${ccy2}, ${from} and ${to}")
      }
    }

    case MaxPriceRequest(ccy1, ccy2, source, date) => {
      try {
        val result = PriceReply(dataExtractor.getHighestPrice(ccy1, ccy2, source, date), "")
        sender ! result
      } catch {
        case ex: Exception => sender ! PriceReply(Option.empty, s"Error: Unable to parse request for ${ccy1}, ${ccy2} and ${date}")
      }
    }

    case MinPriceRequest(ccy1, ccy2, source, date) => {
      try {
        val result = PriceReply(dataExtractor.getLowestPrice(ccy1, ccy2, source, date), "")
        sender ! result
      } catch {
        case ex: Exception => sender ! PriceReply(Option.empty, s"Error: Unable to parse request for ${ccy1}, ${ccy2} and ${date}")
      }
    }

    case MeanPriceRequest(ccy1, ccy2, source, date) => {
      try {
        val result = PriceReply(dataExtractor.getDailyMean(ccy1, ccy2, source, date), "")
        sender ! result
      } catch {
        case ex: Exception => sender ! PriceReply(Option.empty, s"Error: Unable to parse request for ${ccy1}, ${ccy2} and ${date}")
      }
    }

    case ComparisonRequest(firstCcy1, firstCcy2, secondCcy1, secondCcy2, source, date) => {
      try {
        val result = ComparisonReply(dataExtractor.compare(firstCcy1, firstCcy2, secondCcy1, secondCcy2, source, date), "")
        sender ! result
      } catch {
        case ex: Exception => sender ! ComparisonReply(Option.empty, s"Error: Unable to parse request for ${firstCcy1}, ${firstCcy2}, ${secondCcy1}, ${secondCcy2}, ${source} and ${date}")
      }
    }
    
    case ProvidersRequest => {
      try {
        val result = ProvidersReply(dataExtractor.getAllPriceSources(), "")
        sender ! result
      } catch {
        case ex: Exception => sender ! ProvidersReply(Nil, s"Error: Unable to retrieve currency price providers.")
      }
    }
  }
}