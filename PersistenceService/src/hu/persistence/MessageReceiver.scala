package hu.persistence

import hu.monitoring.jms.ActiveMQHandler
import hu.monitoring.jms.HeartBeatSender
import hu.persistence.requestprocessor.AbstractMessageReceiver
import hu.persistence.requestprocessor.LoggingMessageReceiver
import hu.monitoring.MonitoringManager

object ReceiverType extends Enumeration {
  type ReceiverType = Value
  val LOGGING, DATABASE = Value
}

object MessageReceiverFactory {

  def apply(receiverType: ReceiverType.Value): AbstractMessageReceiver = {
    if (receiverType == ReceiverType.LOGGING) {
      new LoggingMessageReceiver
    } else {
      null // No impl yet
    }
  }

  def startMonitoringClient(): Unit = {
    val monitoringManager = MonitoringManager("PersistenceService", new ActiveMQHandler(ParamsSupplier.getParam(ParamsSupplier.BROKER_ENDPOINT), ParamsSupplier.getParam(ParamsSupplier.MONITORING_DESTINATION)))
    monitoringManager.start()
  }

  def main(args: Array[String]) = {
    val receiver = {
      MessageReceiverFactory(ReceiverType.LOGGING).listenOnBackgroundThread
      startMonitoringClient()
      do {
      } while (true)
    }
  }
}