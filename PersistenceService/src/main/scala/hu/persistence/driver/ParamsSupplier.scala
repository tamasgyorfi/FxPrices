package hu.persistence.driver

import hu.staticdataservice.client.HttpClient
import hu.fx.config.ConfigReader
import hu.fx.config.EnvironmentSupplier
import hu.persistence.restapi.RestApiEndpoint

object ParamsSupplier {

  val configReader = new ConfigReader("src/main/resources/props_%s.properties")

  val BROKER_ENDPOINT = "jms.broker.endpoint"
  val MONITORING_DESTINATION = "jms.monitoring.destination"
  val REST_ENDPOINT = "persistence.rest.host"
  val REST_PORT = "persistence.rest.port"
  
  private val parameters: List[String] =
    List(BROKER_ENDPOINT,
      MONITORING_DESTINATION,
      "jms.persistence.destination",
      REST_ENDPOINT,
      REST_PORT)

  private val params = {
    val host = configReader.getProperty("staticdata.server.host").getOrElse("localhost")
    val port = configReader.getProperty("staticdata.server.port").getOrElse("8888")

    HttpClient((host -> port.toInt)).getParameters(EnvironmentSupplier.getEnvironment(), parameters)
  }

  def getParam(key: String)(implicit default: String = "param not available"): String = {
    params.getOrElse(key, default)
  }
}