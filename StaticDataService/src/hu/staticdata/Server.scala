package hu.staticdata

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global

import org.slf4s.LoggerFactory

import akka.io.IO
import akka.pattern.ask
import spray.can.Http

class Server extends StaticDataServiceComponent {

  val logger = LoggerFactory.getLogger(this.getClass)

  def start() = {
    logger info s"Trying to bind StaticDataService at $interface:$port"
    bindService()
  }
  
  def bindService() = {
    val future = IO(Http) ? Http.Bind(service, interface, port)
    Await.ready(future, timeout.duration) map {
      case Http.Bound(host) => logger info s"Service successfully bound on $interface : $port"
      case message          => { logger error s"Error binding service. Message received: $message. Exiting."; System.exit(1) }
    }
  }
}

object StaticDataServer {
  def main(args: Array[String]) {
    new Server().start()
  }
}