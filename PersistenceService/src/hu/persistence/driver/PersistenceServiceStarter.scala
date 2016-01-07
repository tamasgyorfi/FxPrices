package hu.persistence.driver

import hu.monitoring.MonitoringManager
import hu.monitoring.jms.ActiveMQHandler
import hu.persistence.api.DataHandlerType
import hu.persistence.messaging.messagehandling.AbstractMessageReceiver
import hu.persistence.messaging.messagehandling.ForwardingMessageReceiver
import hu.persistence.messaging.messagehandling.ObjectMessageExtractor
import hu.persistence.data.SimpleDataHandlingManager

object PersistenceServiceStarter {

  def apply(receiverType: DataHandlerType.Value): AbstractMessageReceiver = {
    val dataHandlingManager = new SimpleDataHandlingManager(receiverType)
      new ForwardingMessageReceiver(new ObjectMessageExtractor(), dataHandlingManager.getDataPersister())
  }

  def startMonitoringClient(): Unit = {
    val monitoringManager = MonitoringManager("PersistenceService", new ActiveMQHandler(ParamsSupplier.getParam(ParamsSupplier.BROKER_ENDPOINT), ParamsSupplier.getParam(ParamsSupplier.MONITORING_DESTINATION)))
    monitoringManager.start()
  }

  def main(args: Array[String]) = {
    startMonitoringClient()
    val receiver = {
      PersistenceServiceStarter(DataHandlerType.LOGGING).startListening
      startMonitoringClient()
      do {
      } while (true)
    }
  }
}