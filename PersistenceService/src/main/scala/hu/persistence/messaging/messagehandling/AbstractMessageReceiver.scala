package hu.persistence.messaging.messagehandling

import javax.jms.MessageListener
import hu.persistence.messaging.jms.ActiveMQHandler
import hu.persistence.messaging.jms.JmsHandler

abstract class AbstractMessageReceiver(jmsHandler: JmsHandler) extends MessageListener {

  def start = jmsHandler.messageReceiver.setMessageListener(this)
  def startListening

}