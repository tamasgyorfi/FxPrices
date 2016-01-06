package hu.monitoring

import hu.monitoring.jms.ActiveMQHandler
import hu.monitoring.jms.HeartBeatSender
import hu.monitoring.jms.MessageSender
import hu.monitoring.logging.DistributedLogger
import hu.monitoring.logging.Status._

class MonitoringManager(identity: String, mqHandler: ActiveMQHandler) {

  val messageSender: MessageSender = new MessageSender(identity, mqHandler)
  val logger = DistributedLogger(messageSender)
  val heartBeatSender = new HeartBeatSender(messageSender)

  def start() = {
    heartBeatSender.start()
  }

  def report(status: Status, message: String) = {
    logger.report(status, message);
  }
}

object MonitoringManager {
  var manager: MonitoringManager = null

  def apply(identity: String, mqHandler: ActiveMQHandler) = {
    manager = new MonitoringManager(identity, mqHandler)
    manager
  }

  def reportWarning(message: String) = {
    manager.report(WARN, message)
  }

  def reportError(message: String) = {
    manager.report(ERROR, message)
  }

}