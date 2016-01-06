package hu.monitoring.jms

import org.slf4s.LoggerFactory
import javax.jms.Message

class MessageSender(identity: String, mqHandler: ActiveMQHandler) {

  val LOGGER = LoggerFactory.getLogger(this.getClass)

  def sendMessage(messageProducer: Any => Message) {
    try {
      val message = messageProducer()
      message.setStringProperty("sender", identity)

      mqHandler.messageSender.send(mqHandler.messageDestination, message);
      LOGGER.info("message successfully sent")
    } catch {
      case e: Exception => LOGGER.error("Unable to send heartbeat message. ", e)
    }
  }

  def createTextMessage(message: String) = {
    mqHandler.session.createTextMessage(message)
  }

  def createMapMessage(props: Map[String, String]) = {
    val mapMessage = mqHandler.session.createMapMessage()
    props.foreach { case (key, value) => mapMessage.setString(key, value) }

    mapMessage
  }
}