package hu.staticdataservice.client

import org.slf4s.LoggerFactory
import com.mashape.unirest.http.Unirest
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import scala.collection.JavaConversions

private object ObjectMapperHolder {
  private def objectMapper = {
    val objMapper = new ObjectMapper()
    objMapper.registerModule(DefaultScalaModule)

    objMapper
  }

  val mapper = objectMapper
}

class HttpClient(serviceEndpoint: (String, Int)) {

  val logger = LoggerFactory.getLogger(this.getClass)
  val PARAMS_PATH = "/params"

  def this(host:String, port:Integer) {
    this(host->port)
  }
  
  def getParametersFor(environment: String, parameters: java.util.List[String]): java.util.Map[String, String] ={
    JavaConversions.mapAsJavaMap(getParameters(environment, JavaConversions.collectionAsScalaIterable(parameters) toList))
  }
  
  def getParameters(environment: String, parameters: List[String]): Map[String, String] = {
    val csvParams = parameters.reduceRight(_ + "," + _)

    try {
      doQuery(environment, csvParams)
    } catch {
      case e: Exception => {
        logger error ("Error while trying to retrieve parameters. Exception was: ", e)
        Map()
      }
    }
  }

  private def doQuery(environment: String, csvParams: String): Map[String, String] = {
    val queryResult = Unirest
      .get("http://" + serviceEndpoint._1 + ":" + serviceEndpoint._2 + PARAMS_PATH)
      .header("accept", "application/json")
      .queryString("env", environment)
      .queryString("keys", csvParams)
      .asString()
      .getBody

    val res = ObjectMapperHolder.mapper.readValue(queryResult, classOf[Map[String, String]])
    logger info s"Successfully retrieved parameters from Parameter Service: $res"
    res
  }

}
object HttpClient {
  def apply(endpoint: (String, Int)) = new HttpClient(endpoint)
}