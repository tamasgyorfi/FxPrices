package hu.persistence.requestprocessor

import hu.fx.service.persistencerequest.jms.ActiveMQHandler
import javax.jms.Message
import javax.jms.MessageListener
import javax.jms.ObjectMessage
import scala.concurrent.Promise
import java.util.Scanner

class MessageReceiver extends ActiveMQHandler with MessageListener {

  def start = messageReceiver.setMessageListener(this)

  def onMessage(message: Message): Unit = message match {
    case msg: ObjectMessage => println("Object message jott")
    case _                  => println("Csak Object messaget fogadok")
  }
}

object MessageReceiver {
  def apply() = {
    val receiver = new MessageReceiver()
  }

  def listenOnBackgroundThread = {
    new Thread(new Runnable() {
      def run() = {
        new MessageReceiver().start
      }
    }).start()
  }

  def main(args: Array[String]) = {
    val messageReceiver = {
      MessageReceiver.listenOnBackgroundThread
      val scanner = new Scanner(System.in)
      do {
        val input = scanner.next()

        if (input.equals("exit")) System.exit(0)
        else println("Read: " + input)
      } while (true)
    }
  }
}