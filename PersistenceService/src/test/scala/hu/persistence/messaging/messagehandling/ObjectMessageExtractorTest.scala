package hu.persistence.messaging.messagehandling

import org.mockito.Mockito.when
import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import javax.jms.MapMessage
import javax.jms.ObjectMessage
import hu.fx.data.FxQuote
import hu.fx.data.Quote

class ObjectMessageExtractorTest extends FunSuite with MockitoSugar {

  private val sut = new ObjectMessageExtractor()

  test("Extract should return empty list when message payload is not as expected") {
    val message = mock[ObjectMessage]
    when(message.getObject()).thenReturn("STRING", null)

    assert(List() === sut.extract(message))
  }

  test("Extract should return empty list when message is not object message") {
    val message = mock[MapMessage]
    assert(List() === sut.extract(message))
  }

  test("Extract should return extracted message when everything is allright") {
    val message = mock[ObjectMessage]
    val payload: List[Quote] = List(FxQuote("EUR", 1, 11.22, "", "source"), FxQuote("CHF", 1, 1.22, "", "source"))
    when(message.getObject()).thenReturn(payload, null)
    assert(payload === sut.extract(message))
  }

}