package hu.fx.service.main

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import org.slf4s.LoggerFactory
import akka.actor.ActorSystem
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import hu.fx.service.config.ObjectConfig
import hu.fx.service.config.ParamsSupplier
import hu.fx.service.messaging.RequestSender
import hu.monitoring.MonitoringManager
import hu.monitoring.jms.ActiveMQHandler
import spray.can.Http
import akka.actor.Props
import hu.fx.service.pricing.RestApiEndpoint

trait AkkaSystem {
  implicit val system = ActorSystem("StaticDataService")
}
class PriceServiceServer(persistenceRequestSender: RequestSender) extends AkkaSystem {

  implicit val timeout = Timeout(5 seconds)
  val logger = LoggerFactory.getLogger(this.getClass)
 
  private val restServer = system.actorOf(Props(new RestApiEndpoint(persistenceRequestSender)), name = "RequestReceiver")

  private def start() = {
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
}

object PriceServiceServer {
  def main(args: Array[String]): Unit = {
    startMonitoringClient()
    new PriceServiceServer(ObjectConfig.persistenceRequestSender).start()
  }

  def startMonitoringClient(): Unit = {
		  val monitoringManager = MonitoringManager("PricesService", new ActiveMQHandler(ParamsSupplier.getParam(ParamsSupplier.BROKER_ENDPOINT), ParamsSupplier.getParam(ParamsSupplier.MONITORING_DESTINATION)))
				  monitoringManager.start()
  }
}