package hu.staticdata.params.componenttest

import scala.concurrent.duration.DurationInt

import org.scalatest.Matchers
import org.scalatest.WordSpecLike

import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.testkit.ImplicitSender
import akka.testkit.TestKit
import hu.staticdata.params.EnvironmentNotFound
import hu.staticdata.params.ParamValues
import hu.staticdata.params.ParamsActor
import hu.staticdata.params.ParamsReader
import hu.staticdata.params.ParamsRequest

class ComponentTests extends TestKit(ActorSystem("testSystem")) with WordSpecLike with Matchers with ImplicitSender {

  trait DummyValuesParamsReader extends ParamsReader {
    def getAllParameters = Map("default" -> Map("key1" -> "value1", "key2" -> "value2", "key3" -> "value3"),
      "dev" -> Map("key12" -> "value12", "key22" -> "value22", "key32" -> "value32"),
      "uat" -> Map("key19" -> "value19", "key29" -> "value29", "key39" -> "value39"))
  }

  val restActorRef = system.actorOf(Props(new ParamsActor(new DummyValuesParamsReader {}.getAllParameters())), "staticDataService-properties")

  "The properties actor" should {
    "return all requested parameters" in {
      within(500 millis) {
        restActorRef ! ParamsRequest("dev", "key22,key32")
        expectMsg(ParamValues(List(("key22", "value22"), ("key32", "value32"))))
      }
    }

    "return all requested parameters it knows, and UNKNOWN for the rest" in {
      within(500 millis) {
        restActorRef ! ParamsRequest("dev", "key22,key32,oportunidad,key12")
        expectMsg(ParamValues(List(("key22", "value22"), ("key32", "value32"), ("oportunidad", "UNKNOWN"), ("key12", "value12"))))
      }
    }

    "return EnvironmentNotFound when env param is unknown" in {
      within(500 millis) {
        restActorRef ! ParamsRequest("oportunidad", "key22,key32")
        expectMsg(EnvironmentNotFound)
      }
    }

    "return EnvironmentNotFound when an unknown message is received" in {
      within(500 millis) {
        restActorRef ! "give me params"
        expectMsg(EnvironmentNotFound)
      }
    }
  }

}