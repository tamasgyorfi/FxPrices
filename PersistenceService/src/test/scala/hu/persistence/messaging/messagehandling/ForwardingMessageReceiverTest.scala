package hu.persistence.messaging.messagehandling

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.mock.MockitoSugar
import hu.persistence.data.DataPersister
import javax.jms.ObjectMessage
import org.mockito.Mockito._
import hu.persistence.data.mongo.TestData

class ForwardingMessageReceiverTest extends FunSuite with BeforeAndAfter with MockitoSugar with TestData {

  private val messageExtractor = mock[MessageExtractor]
  private val dataPersister = mock[DataPersister]
  private val message = mock[ObjectMessage]

  test("Message receiver should extract message and send payload for persistence") {
    val sut = new ForwardingMessageReceiver(messageExtractor, dataPersister)

    when(messageExtractor.extract(message)).thenReturn(validQuotes)
    sut.onMessage(message)
    verify(dataPersister).save(validQuotes)
  }
}