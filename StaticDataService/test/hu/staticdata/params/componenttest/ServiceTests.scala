package hu.staticdata.params.componenttest

import java.net.URL

import scala.io.Source

import org.scalatest.Matchers
import org.scalatest.WordSpecLike

import hu.staticdata.Server
import hu.staticdata.StaticDataServiceComponent

trait TestData extends StaticDataServiceComponent {
  val serviceUrl = "http://" + interface + ":" + port + "/params?"

  def getParameters(params: String): String = {
    Source.fromInputStream(new URL(serviceUrl + params).openStream()).mkString
  }
}

class ServiceTests extends WordSpecLike with Matchers with TestData {

  "The param service" should {
    val server = new Server().start()

    "returns all parameters it knows and UNKNOWN for the rest" in {

      val result = getParameters("env=default&keys=jms.broker.endpoint,nulla,jms.persistence.destination")
      assert(result contains (""""jms.broker.endpoint": "tcp://localhost:61616""""))
      assert(result contains (""""nulla": "UNKNOWN""""))
      assert(result contains (""""jms.persistence.destination": "queue/persistence""""))
    }

    "returns EnvironmentNotFound when env is unknown" in {

      val result = getParameters("env=devuat&keys=jms.broker.endpoint,nulla,jms.persistence.destination")
      assert(result contains (""""error": "Environment not found""""))
    }

    "returns a single UNKNOWN result when no keys are specified" in {

      val result = getParameters("env=default&keys=")
      assert(result contains (""""": "UNKNOWN""""))
    }

  }
}