package hu.persistence.requestprocessor

import hu.fx.service.persistencerequest.jms.ActiveMQHandler
import javax.jms.MessageListener

abstract class AbstractMessageReceiver extends ActiveMQHandler with MessageListener{

    def start = messageReceiver.setMessageListener(this)
    def listenOnBackgroundThread

}