package hu.persistence.driver

import org.scalatest.WordSpecLike
import org.scalatest.mock.MockitoSugar
import hu.fx.data.FxQuote
import hu.fx.data.Quote
import hu.persistence.FongoFixture
import hu.persistence.config.DatabaseConfig
import javax.jms.ObjectMessage
import org.mockito.Mockito._
import java.net.URL
import scala.io.Source
import javax.jms.MessageConsumer
import hu.persistence.config.Config
import hu.monitoring.MonitoringManager
import hu.persistence.messaging.messagehandling.ObjectMessageExtractor
import hu.persistence.messaging.messagehandling.ForwardingMessageReceiver
import hu.persistence.api.DataHandlerType
import hu.persistence.data.SimpleDataHandlingManager
import com.mongodb.connection.ConnectionFactory
import org.apache.activemq.ActiveMQConnectionFactory
import javax.jms.Connection
import javax.jms.Session
import hu.persistence.messaging.jms.JmsHandler
import hu.persistence.messaging.jms.JmsHandler
import hu.persistence.FongoFixture

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
    FxQuote("RUB", 1, 21.11, "2016-01-13T17:13:13+0000", "YAHOO"))
}

class PersistenceServiceStarterTest extends WordSpecLike with FongoFixture with MockitoSugar {

  private val maxPriceResult = """{"quote":{"ccy2":"INR","quoteUnit":1,"price":10.4,"timestamp":"2016-01-13T17:12:13+0000","source":"YAHOO","ccy1":"USD"},"errorMessage":""}"""
  private val meanPriceResult = """{"quote":{"ccy2":"INR","quoteUnit":1,"price":10.25,"timestamp":"2016-01-13","source":"YAHOO","ccy1":"USD"},"errorMessage":""}"""

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

      assert(10 === sut.collection.count())
    }

    "be able to save and retrieve data as max price" in {

      val maxPrice = callService("http://localhost:9999/maxPrice/YAHOO?ccy1=USD&ccy2=INR&date=2016-01-13")
      assert(maxPriceResult === maxPrice)
    }

    "be able to save and retrieve data as mean price" in {

      val meanPrice = callService("http://localhost:9999/meanPrice/YAHOO?ccy1=USD&ccy2=INR&date=2016-01-13")
      assert(meanPriceResult === meanPrice)
    }
  }

  def callService(path: String) = {
    Source.fromInputStream(new URL(path).openStream()).mkString
  }
}