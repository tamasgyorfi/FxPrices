package hu.persistence.messaging.jms

import org.apache.activemq.ActiveMQConnectionFactory

import hu.persistence.driver.ParamsSupplier
import javax.jms.ConnectionFactory
import javax.jms.Destination
import javax.jms.MessageConsumer
import javax.jms.Session

class ActiveMQHandler extends JmsHandler {

  override def brokerEndpoint = ParamsSupplier.getParam("jms.broker.endpoint")
  override def destinationEndpoint: String = ParamsSupplier.getParam("jms.persistence.destination")

  def connectionFactory: ConnectionFactory = new ActiveMQConnectionFactory(brokerEndpoint)
  def session: Session = {
    val connection = connectionFactory.createConnection()
    connection.start()
    connection.createSession(false, Session.AUTO_ACKNOWLEDGE)
  }
  def messageDestination: Destination = session.createQueue(destinationEndpoint)
  def messageReceiver: MessageConsumer = session.createConsumer(messageDestination)
}