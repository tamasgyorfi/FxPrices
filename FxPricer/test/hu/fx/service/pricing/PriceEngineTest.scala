package hu.fx.service.pricing

import scala.concurrent.duration.DurationInt
import org.scalatest.Matchers
import org.scalatest.WordSpecLike
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.testkit.ImplicitSender
import akka.testkit.TestKit
import hu.fx.data.FxQuote

trait TestData {
  val quotes1 = List(FxQuote("EUR", 1, 1, "", "YAHOO"), FxQuote("HUF", 1, 1, "", "YAHOO"), FxQuote("ROL", 1, 1, "", "YAHOO"))
  val quotes2 = List(FxQuote("EUR", 1, 1, "", "APILAYER"), FxQuote("HUF", 1, 1, "", "APILAYER"), FxQuote("ROL", 1, 1, "", "APILAYER"))
}

class FakePriceEngine extends PriceEngine(null) with TestData {
  override def receive = {
    case ApplicationStart => {
      freshQuotes.updated("YAHOO", quotes1)
      freshQuotes.updated("APILAYER", quotes2)

      context.become(start)
    }
  }
}

class PriceEngineTest extends TestKit(ActorSystem("testSystem")) with WordSpecLike with Matchers with ImplicitSender with TestData {

  val restActorRef = system.actorOf(Props(new FakePriceEngine()), "applicationDriverActor")

  "The driver actor" should {
    "return all quotes where ccy2 matches for QuoteApiRequest" in {
      within(1000 millis) {

        restActorRef ! ApplicationStart
        restActorRef ! QuoteApiRequest("USD", "HUF")
        expectMsg(QuoteApiReply(List(FxQuote("HUF", 1, 1, "", "YAHOO"), FxQuote("HUF", 1, 1, "", "APILAYER"))))
      }
    }
  }

  "The driver actor" should {
    "return all quotes for AllQuotesApiRequest" in {
      within(1000 millis) {

        restActorRef ! ApplicationStart
        restActorRef ! AllQuotesApiRequest
        expectMsg(AllQuotesApiReply(Map().updated("YAHOO", quotes1).updated("APILAYER", quotes2)))
      }
    }
  }
}
