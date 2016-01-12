package hu.persistence.messaging.messagehandling

import javax.jms.MessageListener
import hu.persistence.messaging.jms.ActiveMQHandler

abstract class AbstractMessageReceiver extends ActiveMQHandler with MessageListener{

    def start = messageReceiver.setMessageListener(this)
    def startListening

}