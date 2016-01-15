package hu.persistence.messaging.messagehandling

import org.slf4s.LoggerFactory
import hu.persistence.data.DataPersister
import javax.jms.Message
import javax.jms.ObjectMessage
import hu.monitoring.MonitoringManager
import hu.persistence.messaging.jms.JmsHandler

class ForwardingMessageReceiver(jmsHandler: JmsHandler, messageExtractor: MessageExtractor, dataPersister: DataPersister) extends AbstractMessageReceiver(jmsHandler) {

  val logger = LoggerFactory.getLogger(this.getClass)

  def onMessage(message: Message): Unit = message match {
    case msg: ObjectMessage => {
      logger info s"Object message received $message"

      try {
        val quotes = messageExtractor.extract(msg)
        dataPersister.save(quotes)
      } catch {
        case ex: Exception => {
          handleException(ex)
        }
      }
    }
    case _ => logger info s"Object messages are supported only. Received $message"
  }

  protected def handleException(ex: Exception) = {
    val errorMessage = s"Error while trying to process/save new quotes from price server. Exception was ${ex}"
    logger error errorMessage
    MonitoringManager.reportError(errorMessage)
  }

  def startListening = {
    new Thread(new Runnable() {
      def run() = {
        start
      }
    }).start()
  }

}