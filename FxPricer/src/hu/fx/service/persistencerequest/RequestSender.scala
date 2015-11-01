package hu.fx.service.persistencerequest

import hu.fx.service.api.Quote
import hu.fx.service.persistencerequest.jms.JmsHandler
import javax.jms.JMSException
import org.slf4s.LoggerFactory

class RequestSender(jmsHandler: JmsHandler) {

  private val logger = LoggerFactory.getLogger(this.getClass)

  def sendPersistenceRequest(payload: List[Quote]): Boolean = payload match {
    case Nil => true
    case _ => {
      try {
        logger info "Sending persistence request to persistence service."
        sendRequest(payload)
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

  private def sendRequest(payload: List[Quote]): Unit = {
    val message = jmsHandler.session.createObjectMessage(payload.asInstanceOf[Serializable])
    jmsHandler.messageSender.send(jmsHandler.messageDestination, message)
  }
}

object RequestSender {
  def apply(jmsHandler: JmsHandler) = new RequestSender(jmsHandler)
}