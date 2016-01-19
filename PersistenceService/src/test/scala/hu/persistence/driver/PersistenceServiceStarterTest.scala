package hu.persistence.driver

import java.net.URL

import scala.io.Source

import org.mockito.Mockito.when
import org.scalatest.WordSpecLike
import org.scalatest.mock.MockitoSugar

import hu.fx.data.FxQuote
import hu.fx.data.Quote
import hu.monitoring.MonitoringManager
import hu.persistence.FongoFixture
import hu.persistence.api.DataHandlerType
import hu.persistence.config.Config
import hu.persistence.data.SimpleDataHandlingManager
import hu.persistence.messaging.jms.JmsHandler
import hu.persistence.messaging.messagehandling.ForwardingMessageReceiver
import hu.persistence.messaging.messagehandling.ObjectMessageExtractor
import javax.jms.MessageConsumer
import javax.jms.ObjectMessage

trait TestConfig extends Config with MockitoSugar with FongoFixture {

  val collection = mongoCollection
  val jmsHandler = mock[JmsHandler]
  val messageExtractor = new ObjectMessageExtractor()
  val dataHandlingManager = new SimpleDataHandlingManager(DataHandlerType.DATABASE, collection)
  val monitoringManager = mock[MonitoringManager]
  val receiver = mock[MessageConsumer]
  val messageReceiver = createMessageReceiver()

  def createMessageReceiver() = {
    when(jmsHandler.messageReceiver).thenReturn(receiver)
    new ForwardingMessageReceiver(jmsHandler, messageExtractor, dataHandlingManager.getDataPersister())
  }
}

trait TestMessagePayload {
  val payloadList1: List[Quote] = List(
    FxQuote("HUF", 1, 0.465, "2016-01-13T17:12:13+0000", "YAHOO"),
    FxQuote("KRW", 1, 2.45, "2016-01-13T17:12:13+0000", "YAHOO"),
    FxQuote("RON", 1, 7.65, "2016-01-13T17:12:13+0000", "YAHOO"),
    FxQuote("INR", 1, 10.4, "2016-01-13T17:12:13+0000", "YAHOO"),
    FxQuote("RUB", 1, 21.6, "2016-01-13T17:12:13+0000", "YAHOO"))

  val payloadList2: List[Quote] = List(
    FxQuote("HUF", 1, 0.467, "2016-01-13T17:13:13+0000", "YAHOO"),
    FxQuote("KRW", 1, 2.42, "2016-01-13T17:13:13+0000", "YAHOO"),
    FxQuote("RON", 1, 7.25, "2016-01-13T17:13:13+0000", "YAHOO"),
    FxQuote("INR", 1, 10.1, "2016-01-13T17:13:13+0000", "YAHOO"),
    FxQuote("INR", 1, 10.1, "2016-01-14T17:13:13+0000", "YAHOO"),
    FxQuote("RUB", 1, 21.11, "2016-01-13T17:13:13+0000", "YAHOO"))
}

class PersistenceServiceStarterTest extends WordSpecLike with FongoFixture with MockitoSugar {

  private val maxPriceResult = """{"quote":{"ccy2":"INR","quoteUnit":1,"price":10.4,"timestamp":"2016-01-13T17:12:13+0000","source":"YAHOO","ccy1":"USD"},"errorMessage":""}"""
  private val meanPriceResult = """{"quote":{"ccy2":"INR","quoteUnit":1,"price":10.25,"timestamp":"2016-01-13","source":"YAHOO","ccy1":"USD"},"errorMessage":""}"""
  private val historyResult = """{"quotes":[{"ccy2":"INR","quoteUnit":1,"price":10.4,"timestamp":"2016-01-13T17:12:13+0000","source":"YAHOO","ccy1":"USD"},{"ccy2":"INR","quoteUnit":1,"price":10.1,"timestamp":"2016-01-13T17:13:13+0000","source":"YAHOO","ccy1":"USD"}],"errorMessage":""}"""
  private val historyRangeResult = """{"quotes":[{"ccy2":"INR","quoteUnit":1,"price":10.4,"timestamp":"2016-01-13T17:12:13+0000","source":"YAHOO","ccy1":"USD"},{"ccy2":"INR","quoteUnit":1,"price":10.1,"timestamp":"2016-01-13T17:13:13+0000","source":"YAHOO","ccy1":"USD"},{"ccy2":"INR","quoteUnit":1,"price":10.1,"timestamp":"2016-01-14T17:13:13+0000","source":"YAHOO","ccy1":"USD"}],"errorMessage":""}"""
  private val comparisonResult = """{"comparison":{"quote1":[{"ccy2":"INR","quoteUnit":1,"price":10.4,"timestamp":"2016-01-13T17:12:13+0000","source":"YAHOO","ccy1":"USD"},{"ccy2":"INR","quoteUnit":1,"price":10.1,"timestamp":"2016-01-13T17:13:13+0000","source":"YAHOO","ccy1":"USD"}],"quote2":[{"ccy2":"HUF","quoteUnit":1,"price":0.465,"timestamp":"2016-01-13T17:12:13+0000","source":"YAHOO","ccy1":"USD"},{"ccy2":"HUF","quoteUnit":1,"price":0.467,"timestamp":"2016-01-13T17:13:13+0000","source":"YAHOO","ccy1":"USD"}]},"errorMessage":""}"""
  
  private class Starter() extends PersistenceServiceStarter with TestConfig with TestMessagePayload
  private val sut = new Starter()

  "Application" should {

    "be able to receive JMS messages and save them" in {

      val load1 = mock[ObjectMessage]
      val load2 = mock[ObjectMessage]
      when(load1.getObject).thenReturn(sut.payloadList1, Nil)
      when(load2.getObject).thenReturn(sut.payloadList2, Nil)

      sut.startService()

      sut.messageReceiver.onMessage(load1)
      sut.messageReceiver.onMessage(load2)

      assert(11 === sut.collection.count())
    }

    "be able to retrieve data as max price" in {

      val maxPrice = callService("http://localhost:9999/maxPrice/YAHOO?ccy1=USD&ccy2=INR&date=2016-01-13")
      assert(maxPriceResult === maxPrice)
    }

    "be able to retrieve data as mean price" in {

      val meanPrice = callService("http://localhost:9999/meanPrice/YAHOO?ccy1=USD&ccy2=INR&date=2016-01-13")
      assert(meanPriceResult === meanPrice)
    }

    "be able to retrieve data as quote history on a specific date" in {
      val history = callService("http://localhost:9999/currencyPairHistory/YAHOO?ccy1=USD&ccy2=INR&date=2016-01-13")
      assert(historyResult === history)
    }

    "be able to retrieve data as quote history between two date" in {
      val historyRange = callService("http://localhost:9999/currencyPairHistory/YAHOO?ccy1=USD&ccy2=INR&from=2016-01-13&to=2016-01-14")
      assert(historyRangeResult === historyRange)
    }

    "be able to retrieve data as quote comparison" in {
      val comparison = callService("http://localhost:9999/compare/YAHOO?q1_ccy1=USD&q1_ccy2=INR&q2_ccy1=USD&q2_ccy2=HUF&date=2016-01-13")
      assert(comparisonResult === comparison)
    }

  }

  def callService(path: String) = {
    Source.fromInputStream(new URL(path).openStream()).mkString
  }
}