package hu.monitoring.jms

import org.apache.activemq.ActiveMQConnectionFactory
import javax.jms.ConnectionFactory
import javax.jms.Session
import javax.jms.Destination
import javax.jms.MessageProducer

class ActiveMQHandler(brokerEndpoint: String, destinationEndpoint: String) {

  def connectionFactory: ConnectionFactory = new ActiveMQConnectionFactory(brokerEndpoint)
  def session: Session = {
    val connection = connectionFactory.createConnection()
    connection.start()
    connection.createSession(false, Session.AUTO_ACKNOWLEDGE)
  }
  def messageDestination: Destination = session.createQueue(destinationEndpoint)
  def messageSender: MessageProducer = session.createProducer(messageDestination)
}

object ActiveMQHandler {
  def apply(host:String, queueName:String) = new ActiveMQHandler(host, queueName)
}