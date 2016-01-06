package hu.monitoring.jms

import java.util.concurrent.Executors
import javax.jms.TextMessage
import org.slf4s.LoggerFactory
import java.util.concurrent.TimeUnit

class HeartBeatSender(messageSender: MessageSender) {

  def start() {
    val scheduler = Executors.newSingleThreadScheduledExecutor();
    scheduler.scheduleAtFixedRate(new HeartBeatTask(), 5, 5, TimeUnit.SECONDS)
  }

  private class HeartBeatTask extends Runnable {
    def run(): Unit = {
      messageSender.sendMessage { _ => messageSender.createTextMessage("") }
    }
  }
}