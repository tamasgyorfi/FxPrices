package hu.persistence.config

import hu.persistence.data.DataHandlingManager
import hu.persistence.messaging.messagehandling.AbstractMessageReceiver
import hu.persistence.messaging.messagehandling.MessageExtractor
import hu.monitoring.MonitoringManager

trait Config {
  def messageExtractor: MessageExtractor
  def dataHandlingManager: DataHandlingManager
  def messageReceiver: AbstractMessageReceiver
  def monitoringManager: MonitoringManager
}