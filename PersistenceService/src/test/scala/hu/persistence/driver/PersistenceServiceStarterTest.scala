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
import hu.persistence.restapi.PriceReply
import hu.persistence.QuoteDeserializer
import hu.persistence.restapi.CurrencyHistoryReply
import hu.persistence.restapi.ComparisonReply
import hu.persistence.api.QuoteComparison
import hu.persistence.restapi.ProvidersReply

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
  val huf1 = FxQuote("HUF", 1, 0.465, "2016-01-13T17:12:13+0000", "YAHOO")
  val krw1 = FxQuote("KRW", 1, 2.45, "2016-01-13T17:12:13+0000", "YAHOO")
  val ron1 = FxQuote("RON", 1, 7.65, "2016-01-13T17:12:13+0000", "YAHOO")
  val inr1 = FxQuote("INR", 1, 10.4, "2016-01-13T17:12:13+0000", "YAHOO")
  val rub1 = FxQuote("RUB", 1, 21.6, "2016-01-13T17:12:13+0000", "YAHOO")

  val huf2 = FxQuote("HUF", 1, 0.467, "2016-01-13T17:13:13+0000", "YAHOO")
  val krw2 = FxQuote("KRW", 1, 2.42, "2016-01-13T17:13:13+0000", "YAHOO")
  val ron2 = FxQuote("RON", 1, 7.25, "2016-01-13T17:13:13+0000", "YAHOO")
  val inr2 = FxQuote("INR", 1, 10.1, "2016-01-13T17:13:13+0000", "YAHOO")
  val inr3 = FxQuote("INR", 1, 10.1, "2016-01-14T17:13:13+0000", "YAHOO")
  val rub2 = FxQuote("RUB", 1, 21.11, "2016-01-13T17:13:13+0000", "YAHOO")

  val payloadList1: List[Quote] = List(
    huf1,
    krw1,
    ron1,
    inr1,
    rub1)

  val payloadList2: List[Quote] = List(
    huf2,
    krw2,
    ron2,
    inr2,
    inr3,
    rub2)
}

class PersistenceServiceStarterTest extends WordSpecLike with FongoFixture with MockitoSugar with TestMessagePayload {

  private val maxPriceResult = PriceReply(Some(inr1), "")
  private val meanPriceResult = PriceReply(Some(FxQuote("INR", 1, 10.25, "2016-01-13", "YAHOO")), "")
  private val historyResult = CurrencyHistoryReply(List(inr1, inr2), "")
  private val historyRangeResult = CurrencyHistoryReply(List(inr1, inr2, inr3), "")
  private val comparisonResult = ComparisonReply(Some(new QuoteComparison(List(inr1, inr2), List(huf1, huf2))), "")
  private val providersResult = ProvidersReply(List("YAHOO"), "")
  
  private class Starter() extends PersistenceServiceStarter with TestConfig {
    val restHost = "localhost"
    val restPort = 9999;
  }
  private val sut = new Starter()

  "Application" should {

    "be able to receive JMS messages and save them" in {

      val load1 = mock[ObjectMessage]
      val load2 = mock[ObjectMessage]
      when(load1.getObject).thenReturn(payloadList1, Nil)
      when(load2.getObject).thenReturn(payloadList2, Nil)

      sut.startService()

      sut.messageReceiver.onMessage(load1)
      sut.messageReceiver.onMessage(load2)

      assert(11 === sut.collection.count())
    }

    "be able to retrieve data as max price" in {

      val maxPrice = callService("http://localhost:9999/currencies/YAHOO/USD/INR/aggregate?filter=max&date=2016-01-13")
      assert(maxPriceResult === QuoteDeserializer.mapper.readValue(maxPrice, classOf[PriceReply]))
    }

    "be able to retrieve data as mean price" in {

      val meanPrice = callService("http://localhost:9999/currencies/YAHOO/USD/INR/aggregate?filter=avg&date=2016-01-13")
      assert(meanPriceResult === QuoteDeserializer.mapper.readValue(meanPrice, classOf[PriceReply]))
    }

    "be able to retrieve data as quote history on a specific date" in {
      val history = callService("http://localhost:9999/currencies/YAHOO/USD/INR?date=2016-01-13")
      assert(historyResult === QuoteDeserializer.mapper.readValue(history, classOf[CurrencyHistoryReply]))
    }

    "be able to retrieve data as quote history between two dates" in {
      val historyRange = callService("http://localhost:9999/currencies/YAHOO/USD/INR?from=2016-01-13&to=2016-01-14")
      assert(historyRangeResult === QuoteDeserializer.mapper.readValue(historyRange, classOf[CurrencyHistoryReply]))
    }

    "be able to retrieve data as quote comparison" in {
      val comparison = callService("http://localhost:9999/currencies/YAHOO/comparison?q1_ccy1=USD&q1_ccy2=INR&q2_ccy1=USD&q2_ccy2=HUF&date=2016-01-13")
      assert(comparisonResult === QuoteDeserializer.mapper.readValue(comparison, classOf[ComparisonReply]))
    }
    
    "be able to retrieve all the price providers" in {
      val comparison = callService("http://localhost:9999/providers")
      assert(providersResult === QuoteDeserializer.mapper.readValue(comparison, classOf[ProvidersReply]))      
    }
  }

  def callService(path: String) = {
    Source.fromInputStream(new URL(path).openStream()).mkString
  }
}