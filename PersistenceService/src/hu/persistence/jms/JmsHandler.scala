package hu.persistence.jms

import javax.jms.Session
import javax.jms.ConnectionFactory
import javax.jms.Destination
import javax.jms.MessageProducer
import javax.jms.MessageConsumer

trait JmsHandler {

  implicit def brokerEndpoint : String = "localhost:9988"
  implicit def destinationEndpoint : String = "persistenceQueue"
  
  def connectionFactory : ConnectionFactory
  def session: Session 
  def messageDestination : Destination
  def messageReceiver : MessageConsumer
}