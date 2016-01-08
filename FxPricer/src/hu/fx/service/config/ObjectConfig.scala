package hu.fx.service.config

import hu.fx.service.messaging.PersistenceRequestSender
import hu.fx.service.messaging.jms.ActiveMQHandler

object ObjectConfig {
  private val mqHandler = new ActiveMQHandler()
  val persistenceRequestSender = new PersistenceRequestSender(mqHandler)
}