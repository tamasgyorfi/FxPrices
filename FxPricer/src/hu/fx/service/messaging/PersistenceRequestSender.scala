package hu.fx.service.messaging

import org.slf4s.LoggerFactory

import hu.fx.data.Quote
import hu.fx.service.messaging.jms.JmsHandler
import javax.jms.JMSException

class PersistenceRequestSender(jmsHandler: JmsHandler) extends RequestSender {

  private val logger = LoggerFactory.getLogger(this.getClass)

  def sendRequest(payload: List[Quote]): Boolean = payload match {
    // TODO revert this when messages should be sent:
    // case Nil => true 
    case _ => true
    case _ => {
      try {
        logger info "Sending persistence request to persistence service."
        sendPersistenceRequest(payload)
        logger info "Persistence request successfully sent to persistence service."
        true
      } catch {
        case e: JMSException => {
          logger error ("JMSException caught while sending persistence request", e)
          false
        }
        case e: Exception => {
          logger error ("Exception caught while sending persistence request", e)
          false
        }
      }
    }
  }

  private def sendPersistenceRequest(payload: List[Quote]): Unit = {
    val message = jmsHandler.session.createObjectMessage(payload.asInstanceOf[Serializable])
    jmsHandler.messageSender.send(jmsHandler.messageDestination, message)
  }
}

object PersistenceRequestSender {
  def apply(jmsHandler: JmsHandler) = new PersistenceRequestSender(jmsHandler)
}