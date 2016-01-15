package hu.persistence.config

import hu.persistence.api.DataHandlerType
import hu.persistence.data.SimpleDataHandlingManager
import hu.persistence.messaging.messagehandling.ForwardingMessageReceiver
import hu.persistence.messaging.messagehandling.ObjectMessageExtractor
import hu.monitoring.MonitoringManager
import hu.persistence.driver.ParamsSupplier
import hu.monitoring.jms.ActiveMQHandler
import com.mongodb.casbah.MongoClient

trait DatabaseConfig extends Config {
  val SERVER = "localhost"
  val PORT = 27017
  val DATABASE = "fxprices"
  val COLLECTION = "quotes"

  val connection = MongoClient(SERVER, PORT)
  val collection = connection(DATABASE)(COLLECTION)

  val jmsHandler = new hu.persistence.messaging.jms.ActiveMQHandler()
  val messageExtractor = new ObjectMessageExtractor()
  val dataHandlingManager = new SimpleDataHandlingManager(DataHandlerType.DATABASE, collection)
  val messageReceiver = new ForwardingMessageReceiver(jmsHandler, messageExtractor, dataHandlingManager.getDataPersister())
  val monitoringManager = MonitoringManager("PersistenceService", new ActiveMQHandler(ParamsSupplier.getParam(ParamsSupplier.BROKER_ENDPOINT), ParamsSupplier.getParam(ParamsSupplier.MONITORING_DESTINATION)))

}