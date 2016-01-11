package hu.environment.messagebroker

class BrokerMessage

case object StartBroker extends BrokerMessage
case object BrokerStarted extends BrokerMessage
case class BrokerStartError(error: Exception) extends BrokerMessage
