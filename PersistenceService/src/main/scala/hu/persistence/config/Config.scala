package hu.persistence.config

import hu.persistence.data.DataHandlingManager
import hu.persistence.messaging.messagehandling.AbstractMessageReceiver
import hu.persistence.messaging.messagehandling.MessageExtractor
import hu.monitoring.MonitoringManager
import hu.persistence.messaging.jms.JmsHandler

trait Config {
  def messageExtractor: MessageExtractor
  def dataHandlingManager: DataHandlingManager
  def messageReceiver: AbstractMessageReceiver
  def monitoringManager: MonitoringManager
  def jmsHandler: JmsHandler
  
  def restHost: String
  def restPort: Int
}