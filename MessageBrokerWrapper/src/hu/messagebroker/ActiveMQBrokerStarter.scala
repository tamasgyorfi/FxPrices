package hu.messagebroker

import org.apache.activemq.broker.BrokerService
import hu.fx.service.ParamsSupplier
import hu.monitoring.jms.HeartBeatSender
import hu.monitoring.jms.ActiveMQHandler

class ActiveMQBrokerStarter {
  def startBroker(): Unit = {
    val broker = new BrokerService()
    broker.addConnector(ParamsSupplier.getParam("jms.broker.endpoint"))
    broker.start()
  }
  
  def startMonitoringClient(): Unit = {
    val heartBeatSender = new HeartBeatSender("MessageBrokerWrapperService", new ActiveMQHandler(ParamsSupplier.getParam(ParamsSupplier.BROKER_ENDPOINT), ParamsSupplier.getParam(ParamsSupplier.MONITORING_DESTINATION)))
    heartBeatSender.start()
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