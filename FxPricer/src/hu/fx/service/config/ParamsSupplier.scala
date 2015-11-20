package hu.fx.service.config

import hu.staticdataservice.client.HttpClient

object ParamsSupplier {

  private val parameters: List[String] =
    List("service.yahoo.prices",
      "service.apilayer.prices",
      "jms.broker.endpoint",
      "jms.persistence.destination",
      "fxpricer.rest.host",
      "fxpricer.rest.port")

  private val params = {
    val host = ConfigReader.getProperty("staticdata.server.host").getOrElse("localhost")
    val port = ConfigReader.getProperty("staticdata.server.port").getOrElse("8888")
    
    HttpClient(host-> port.toInt).getParameters("default", parameters)
  }

  def getParam(key: String)(implicit default: String = "param not available"): String = {
    params.getOrElse(key, default)
  }
}