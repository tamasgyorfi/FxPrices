package hu.staticdata

import akka.actor.ActorSystem
import scala.concurrent.duration.DurationInt
import hu.staticdata.params.IOParamsReader
import akka.util.Timeout
import akka.actor.Props
import hu.fx.config.ConfigReader

trait StaticDataServiceComponent {

  val paramsReader = IOParamsReader()
  val actorSystem = ActorSystem("StaticDataService")

  val interface = ConfigReader.getProperty("interface").getOrElse("localhost")
  val port = ConfigReader.getProperty("port").getOrElse("8000") toInt

  implicit val timeout = Timeout(5 seconds)
  implicit val system = actorSystem
  lazy val service = system.actorOf(Props(RestActor(paramsReader)), "RestActor")

}