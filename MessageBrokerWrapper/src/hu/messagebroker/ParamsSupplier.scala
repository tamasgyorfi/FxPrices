package hu.fx.service

import hu.staticdataservice.client.HttpClient

object ParamsSupplier {

  private val parameters: List[String] =
    List("jms.broker.endpoint")
      
  private val params = HttpClient(("localhost"->8888)).getParameters("default", parameters)

  def getParam(key: String)(implicit default: String = "param not available"): String = {
    params.getOrElse(key, default)
  }
}