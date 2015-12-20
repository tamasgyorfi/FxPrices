package hu.monitoring.jms

import java.util.concurrent.Executors
import javax.jms.TextMessage
import org.slf4s.LoggerFactory
import java.util.concurrent.TimeUnit

class HeartBeatSender(identity: String, mqHandler: ActiveMQHandler) {

  val LOGGER = LoggerFactory.getLogger(this.getClass)
  
  def start() {
    val scheduler = Executors.newSingleThreadScheduledExecutor();
    scheduler.scheduleAtFixedRate(new HeartBeatTask(), 5, 5, TimeUnit.SECONDS)
  }

  private class HeartBeatTask extends Runnable {
    def run(): Unit = {
      try {
        val message = mqHandler.session.createTextMessage()
        message.setStringProperty("sender", identity)

        mqHandler.messageSender.send(mqHandler.messageDestination, message);
        LOGGER.info("message successfully sent")
      } catch {
        case e: Exception => LOGGER.error("Unable to send heartbeat message. ", e)
      }
    }
  }
}