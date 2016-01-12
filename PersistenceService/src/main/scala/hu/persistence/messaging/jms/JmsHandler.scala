package hu.persistence.messaging.jms

import javax.jms.ConnectionFactory
import javax.jms.Destination
import javax.jms.MessageConsumer
import javax.jms.Session

trait JmsHandler {

  implicit def brokerEndpoint : String = "localhost:9988"
  implicit def destinationEndpoint : String = "persistenceQueue"
  
  def connectionFactory : ConnectionFactory
  def session: Session 
  def messageDestination : Destination
  def messageReceiver : MessageConsumer
}