package hu.fx.service

import hu.staticdataservice.client.HttpClient
import hu.fx.config.EnvironmentSupplier
import hu.fx.config.ConfigReader

object ParamsSupplier {

  val BROKER_ENDPOINT = "jms.broker.endpoint"
  val MONITORING_DESTINATION = "jms.monitoring.destination"
  
  private val parameters: List[String] =
    List(BROKER_ENDPOINT,
      MONITORING_DESTINATION,
      "fxpricer.rest.host",
      "fxpricer.rest.port")

  private val params = {
    val host = ConfigReader.getProperty("staticdata.server.host").getOrElse("localhost")
    val port = ConfigReader.getProperty("staticdata.server.port").getOrElse("8888")

    HttpClient(host -> port.toInt).getParameters(EnvironmentSupplier.getEnvironment(), parameters)
  }

  def getParam(key: String)(implicit default: String = "param not available"): String = {
    params.getOrElse(key, default)
  }
}