package hu.persistence.driver

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import org.slf4s.LoggerFactory
import akka.actor.ActorSystem
import akka.actor.Props
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import hu.persistence.config.Config
import hu.persistence.config.DatabaseConfig
import spray.can.Http
import hu.persistence.restapi.RestApiEndpoint

trait PersistenceServiceStarter extends Config {

  implicit val system = ActorSystem("PersistenceService")
  implicit val timeout = Timeout(5 seconds)
  val logger = LoggerFactory.getLogger(this.getClass)

  def startService() = {
    messageReceiver.startListening
    startMonitoringClient()
    bindRestService()
  }

  private def startMonitoringClient(): Unit = {
    monitoringManager.start()
  }

  private def bindRestService() = {
    val restServer = system.actorOf(Props(new RestApiEndpoint(dataHandlingManager.getDataExtractor())), name = "RequestReceiver")
    val future = IO(Http) ? Http.Bind(restServer, "localhost", 9999)

    Await.ready(future, timeout.duration) map {
      case Http.Bound(host) => logger info s"Service successfully bound"
      case message => {
        logger error s"Error binding service. Message received: $message. Exiting."
        System.exit(1)
      }
    }
  }
}

object PersistenceServerStarter {
  def main(args: Array[String]) = {
    val receiver = {
      class Starter() extends PersistenceServiceStarter with DatabaseConfig
      val starter = new Starter()
      starter.startService()
    }
  }
}

object PersistenceServiceLocal {
  def main(args: Array[String]) = {
    // Can be used for testing, will not connect to other services
    // new DatabaseDataExtractor(MongoConfig.collection).
  }
}