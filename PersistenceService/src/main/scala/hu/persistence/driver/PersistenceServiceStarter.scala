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
import hu.persistence.data.mongo.DatabaseDataExtractor
import org.scalatest.TestData
import java.time.LocalDate
import hu.persistence.FongoFixture

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
    class Starter() extends PersistenceServiceStarter with DatabaseConfig
    val starter = new Starter()
    val future = starter.startService()
  }
}

object PersistenceServiceLocal {
  def main(args: Array[String]) = {
    class Starter() extends FongoFixture
    val c = new Starter()
    c.insertTestData()
    val n = new DatabaseDataExtractor(c.mongoCollection).getDailyMean("USD", "PALLADIUM 1 OZ", "YAHOO", LocalDate.of(2016, 1, 8))
    /*
  		 Can be used for testing, will not connect to other services
	     new DatabaseDataExtractor(mongoCollection).
    */
  }
}