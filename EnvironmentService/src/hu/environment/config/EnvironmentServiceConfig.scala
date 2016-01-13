package hu.environment.config

import scala.concurrent.duration.DurationInt
import akka.actor.ActorSystem
import akka.actor.Props
import akka.util.Timeout
import hu.fx.config.ConfigReader
import hu.environment.params.IOParamsReader
import hu.environment.params.ParamRequestServer
import hu.environment.messagebroker.ActiveMQBrokerStarter
import hu.environment.params.RestApiEndpoint

trait EnvironmentServiceConfig {

  val configReader = new ConfigReader("resources/props_%s.properties")
  val paramsReader = IOParamsReader()
  val actorSystem = ActorSystem("StaticDataService")

  val interface = configReader.getProperty("interface").getOrElse("localhost")
  val port = configReader.getProperty("port").getOrElse("8000") toInt

  implicit val timeout = Timeout(50000 seconds)
  implicit val system = actorSystem

  val paramsServer = system.actorOf(Props(new ParamRequestServer(paramsReader.getAllParameters())), "ParamsServer")
  val restService = system.actorOf(Props(new RestApiEndpoint(paramsServer)), "RestServer")
  val brokerStarter = system.actorOf(Props(new ActiveMQBrokerStarter(paramsServer)), "MqBrokerStarter")

}