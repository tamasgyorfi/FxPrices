package hu.persistence.messaging.messagehandling

import hu.fx.data.Quote
import javax.jms.Message
import javax.jms.ObjectMessage

class ObjectMessageExtractor extends MessageExtractor {
  
  def extract(message: Message): List[Quote] = {
    if (message != null && message.isInstanceOf[ObjectMessage]) {
      val payload = message.asInstanceOf[ObjectMessage].getObject

      if (payload != null && payload.isInstanceOf[List[Quote @unchecked]]) {
        payload.asInstanceOf[List[Quote]]
      } else {
        List()
      }
    } else {
      List()
    }
  }
  
}