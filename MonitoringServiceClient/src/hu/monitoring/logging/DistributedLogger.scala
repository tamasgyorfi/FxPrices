package hu.monitoring.logging

import hu.monitoring.jms.MessageSender

object Status extends Enumeration {
	type Status = Value
			val WARN, ERROR = Value
}

class DistributedLogger(messageSender: MessageSender) {

  import Status._
  def report(status: Status, payload: String): Unit = {
    messageSender.sendMessage { _ => createReportMessage(status, payload) }
  }

  private def createReportMessage(status: Status, payload: String) = {
    val message = messageSender.createMapMessage(Map(
      "timestamp" -> System.currentTimeMillis().toString(),
      "message" -> payload,
      "severity" -> status.toString()))
    message
  }
}

object DistributedLogger {
  def apply(messageSender: MessageSender) = {
    new DistributedLogger(messageSender)
  }
}