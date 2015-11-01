package hu.fx.service

import hu.staticdataservice.client.HttpClient

object ParamsSupplier {

  private val parameters: List[String] =
    List("service.yahoo.prices",
      "service.apilayer.prices",
      "jms.broker.endpoint",
      "jms.persistence.destination")
      
  private val params = HttpClient(("localhost"->8888)).getParameters("default", parameters)

  def getParam(key: String)(implicit default: String = "param not available"): String = {
    params.getOrElse(key, default)
  }
}