package hu.persistence.driver

import hu.monitoring.MonitoringManager
import hu.monitoring.jms.ActiveMQHandler
import hu.persistence.api.DataHandlerType
import hu.persistence.messaging.messagehandling.AbstractMessageReceiver
import hu.persistence.messaging.messagehandling.ForwardingMessageReceiver
import hu.persistence.messaging.messagehandling.ObjectMessageExtractor
import hu.persistence.data.SimpleDataHandlingManager
import hu.persistence.data.mongo.DatabaseDataExtractor
import java.time.LocalDate
import hu.persistence.data.mongo.DatabaseDataExtractor
import hu.persistence.config.MongoConfig
import hu.persistence.config.Config

object PersistenceServiceStarter {

  def apply(receiverType: DataHandlerType.Value): AbstractMessageReceiver = {
    val dataHandlingManager = new SimpleDataHandlingManager(receiverType, MongoConfig.collection)
      new ForwardingMessageReceiver(Config.messageExtractor, dataHandlingManager.getDataPersister())
  }

  def startMonitoringClient(): Unit = {
    val monitoringManager = MonitoringManager("PersistenceService", new ActiveMQHandler(ParamsSupplier.getParam(ParamsSupplier.BROKER_ENDPOINT), ParamsSupplier.getParam(ParamsSupplier.MONITORING_DESTINATION)))
    monitoringManager.start()
  }

  def main(args: Array[String]) = {
    startMonitoringClient()
    val receiver = {
      PersistenceServiceStarter(DataHandlerType.DATABASE).startListening
      startMonitoringClient()
      do {
      } while (true)
    }
  }
}

object PersistenceServiceLocal {
  def main(args: Array[String]) = {
    // Can be used for testing, will not connect to other services
    // new DatabaseDataExtractor(MongoConfig.collection).
  }
}