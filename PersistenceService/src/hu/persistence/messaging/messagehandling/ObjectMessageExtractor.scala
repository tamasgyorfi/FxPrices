package hu.persistence.messaging.messagehandling

import hu.fx.data.Quote
import javax.jms.Message
import javax.jms.ObjectMessage

class ObjectMessageExtractor extends MessageExtractor{
  def extract(message:Message): List[Quote] = {
    val objMessage = message.asInstanceOf[ObjectMessage]
    null
  }
}