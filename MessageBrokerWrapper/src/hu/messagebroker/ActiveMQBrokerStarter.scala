package hu.messagebroker

import org.apache.activemq.broker.BrokerService
import hu.fx.service.ParamsSupplier

class ActiveMQBrokerStarter {
  def startBroker(): Unit = {
    val broker = new BrokerService()
    broker.addConnector(ParamsSupplier.getParam("jms.broker.endpoint"))
    broker.start()
  }
}

object ActiveMQBrokerStarter {
  def start() : Unit = new ActiveMQBrokerStarter().startBroker()
  
  def main(args : Array[String]) = {
    start
  }
}