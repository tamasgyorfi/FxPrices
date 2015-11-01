package hu.staticdata.params

import org.slf4s.LoggerFactory

import akka.actor.Actor
import akka.actor.actorRef2Scala

class ParamsActor(params: Map[String, Map[String, String]]) extends Actor {

  val logger = LoggerFactory.getLogger(this.getClass)

  def receive() = {
    case ParamsRequest(env: String, csvParams: String) => {
      logger info s"Received parameter request for env: $env and params: $csvParams"

      val environmentParams = params.get(env)
      if (environmentParams == None) sender ! EnvironmentNotFound
      else {
        val requestedParams = csvParamsToList(csvParams)
        val paramResults = requestedParams map (param => param -> environmentParams.get.getOrElse(param, "UNKNOWN"))
        sender ! ParamValues(paramResults)
      }
    }

    case _ => sender ! EnvironmentNotFound
  }

  private def csvParamsToList(csvParams: String): List[String] = csvParams split (",") toList
}