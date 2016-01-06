package hu.messagebroker

import org.apache.activemq.broker.BrokerService
import hu.fx.service.ParamsSupplier
import hu.monitoring.jms.HeartBeatSender
import hu.monitoring.jms.ActiveMQHandler
import hu.monitoring.MonitoringManager

class ActiveMQBrokerStarter {
  def startBroker(): Unit = {
    val broker = new BrokerService()
    broker.addConnector(ParamsSupplier.getParam("jms.broker.endpoint"))
    broker.start()
  }
  
  def startMonitoringClient(): Unit = {
    val monitoringManager = MonitoringManager("MessageBrokerWrapperService", new ActiveMQHandler(ParamsSupplier.getParam(ParamsSupplier.BROKER_ENDPOINT), ParamsSupplier.getParam(ParamsSupplier.MONITORING_DESTINATION)))
    monitoringManager.start()
  }
}

object ActiveMQBrokerStarter {
  def start() : Unit = {
    val brokerWrapper = new ActiveMQBrokerStarter()
    
    brokerWrapper.startBroker()
    brokerWrapper.startMonitoringClient()
  }
  
  def main(args : Array[String]) = {
    start
  }
}