package hu.fx.service.main

import akka.io.IO
import akka.pattern.ask
import spray.can.Http
import scala.concurrent.Await
import akka.actor.ActorSystem
import akka.actor.Props
import org.slf4s.LoggerFactory
import scala.concurrent.ExecutionContext.Implicits.global
import akka.util.Timeout
import scala.concurrent.duration.DurationInt
import hu.fx.service.config.ParamsSupplier
import hu.monitoring.jms.HeartBeatSender
import hu.monitoring.jms.ActiveMQHandler

trait AkkaSystem {
  implicit val system = ActorSystem("StaticDataService")
  implicit val timeout = Timeout(5 seconds)

  val restServer = system.actorOf(Props[RestActor], name = "RestActor")
}

class Server extends AkkaSystem {

  val logger = LoggerFactory.getLogger(this.getClass)

  def bindService() = {
    val future = IO(Http) ? Http.Bind(restServer,
      ParamsSupplier.getParam("fxpricer.rest.host"),
      ParamsSupplier.getParam("fxpricer.rest.port").toInt)

    Await.ready(future, timeout.duration) map {
      case Http.Bound(host) => logger info s"Service successfully bound"
      case message => {
        logger error s"Error binding service. Message received: $message. Exiting."
        System.exit(1)
      }
    }
  }

  def startMonitoringClient(): Unit = {
    val heartBeatSender = new HeartBeatSender("PricesService", new ActiveMQHandler(ParamsSupplier.getParam(ParamsSupplier.BROKER_ENDPOINT), ParamsSupplier.getParam(ParamsSupplier.MONITORING_DESTINATION)))
    heartBeatSender.start()
  }

}

object Server {
  def main(args: Array[String]): Unit = {
    val server = new Server()
    server.startMonitoringClient()
    server.bindService()
  }
}