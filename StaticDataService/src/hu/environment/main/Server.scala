package hu.environment.main

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import org.slf4s.LoggerFactory
import akka.io.IO
import akka.pattern.ask
import hu.environment.messagebroker.BrokerStartError
import hu.environment.messagebroker.BrokerStarted
import hu.environment.messagebroker.StartBroker
import spray.can.Http
import hu.environment.config.EnvironmentServiceConfig

class Server extends EnvironmentServiceConfig {

  private val logger = LoggerFactory.getLogger(this.getClass)

  def start() = {
    startActiveMq()
    bindService()
  }

  private def startActiveMq() {
    logger info "Trying to start messaging service"

    val future = (brokerStarter ? StartBroker)
    Await.ready(future, timeout.duration) map {
      case BrokerStarted => logger info s"Messaging service successfully started!"
      case BrokerStartError(error) => {
        logger error s"Error starting messaging service. Message received: error. Exiting."
        System.exit(1)
      }
    }
  }

  private def bindService() = {
    logger info s"Trying to bind StaticDataService at $interface:$port"

    val future = IO(Http) ? Http.Bind(restService, interface, port)
    Await.ready(future, timeout.duration) map {
      case Http.Bound(host) => logger info s"Service successfully bound on $interface : $port"
      case message => {
        logger error s"Error binding service. Message received: $message. Exiting."
        System.exit(1)
      }
    }
  }
}

object StaticDataServer {
  def main(args: Array[String]) {
    new Server().start()
  }
}