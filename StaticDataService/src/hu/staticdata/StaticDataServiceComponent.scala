package hu.staticdata

import akka.actor.ActorSystem
import scala.concurrent.duration.DurationInt
import hu.staticdata.config.ConfigSupplier
import hu.staticdata.params.IOParamsReader
import akka.util.Timeout
import akka.actor.Props

trait StaticDataServiceComponent {

  val paramsReader = IOParamsReader()
  val actorSystem = ActorSystem("StaticDataService")

  val interface = ConfigSupplier.getProperty("interface").getOrElse("localhost")
  val port = ConfigSupplier.getProperty("port").getOrElse("8000") toInt

  implicit val timeout = Timeout(5 seconds)
  implicit val system = actorSystem
  lazy val service = system.actorOf(Props(RestActor(paramsReader)), "RestActor")

}