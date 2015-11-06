package hu.persistence.requestprocessor

import javax.jms.Message
import javax.jms.ObjectMessage
import org.slf4s.LoggerFactory

class LoggingMessageReceiver extends AbstractMessageReceiver {

  val logger = LoggerFactory.getLogger(this.getClass)
  
  def onMessage(message: Message): Unit = message match {
    case msg: ObjectMessage => logger info s"Object message received $message"
    case _                  => logger info s"Object messages are supported only. Received $message"
  }

  def listenOnBackgroundThread = {
    new Thread(new Runnable() {
      def run() = {
        start
      }
    }).start()
  }

}