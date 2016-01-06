package hu.fx

import java.net.URL
import org.slf4s.LoggerFactory
import scala.io.Source
import hu.monitoring.MonitoringManager

package object service {

  val logger = LoggerFactory.getLogger(this.getClass)

  def retrievePricesAsString(priceSourceUri: String): String = {
    val stream = new URL(priceSourceUri).openStream()
    Source.fromInputStream(stream).mkString
  }

  def measureFunctionRuntime[A, B](f: A => B, param: A)(implicit methodName: String = "measureFunctionRuntime"): B = {
    val startTime = System.currentTimeMillis()
    val retVal: B = f.apply(param)
    val endTime = System.currentTimeMillis()

    logger debug "Method " + methodName + "run in " + (endTime - startTime) + " milliseconds"
    retVal
  }

}