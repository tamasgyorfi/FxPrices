package hu.fx.service.pricing

import org.scalatest.BeforeAndAfterAll
import org.scalatest.FunSuite
import org.scalatest.FunSuite
import akka.actor.ActorRef
import akka.actor.Props
import spray.httpx.SprayJsonSupport._
import spray.routing.HttpService
import spray.testkit.Specs2RouteTest
import akka.actor.Actor
import hu.fx.service.messaging.PersistenceRequestSender
import org.scalatest.mock.MockitoSugar
import hu.fx.service.GlobalTestData

class CurrenciesRouteTest extends FunSuite with Specs2RouteTest with HttpService with CurrenciesRoute with MockitoSugar {
  def is = null
  def actorRefFactory = system
  implicit def ta = system
  val appDriver = actorRefFactory.actorOf(Props[TestPriceEngine], name = "PriceEngine")
  val requestSender = mock[PersistenceRequestSender]

  def receive = null
  
  // TODO replace this with less fragile expected data
  val oneCurrencyPair = """{"quotes":[{"ccy2":"KRW","quoteUnit":0,"price":1204.14502,"timestamp":"2016-01-11T15:11:09+0000","source":"YAHOO","ccy1":"USD"},{"ccy2":"KRW","quoteUnit":1,"price":1204.234985,"timestamp":"2016-01-11T17:12:13+0000","source":"APILAYER","ccy1":"USD"},{"ccy2":"KRW","quoteUnit":1,"price":1204.234985,"timestamp":"2016-01-11T17:12:13+0000","source":"ABS","ccy1":"USD"},{"ccy2":"KRW","quoteUnit":1,"price":1204.234985,"timestamp":"2016-01-11T17:12:13+0000","source":"HLC","ccy1":"USD"}],"errorMessage":""}"""
  val allCurrencyPairs = """{"quotes":{"YAHOO":[{"ccy2":"KRW","quoteUnit":0,"price":1204.14502,"timestamp":"2016-01-11T15:11:09+0000","source":"YAHOO","ccy1":"USD"},{"ccy2":"HUF","quoteUnit":0,"price":104.14502,"timestamp":"2016-01-11T15:11:09+0000","source":"YAHOO","ccy1":"USD"},{"ccy2":"CHF","quoteUnit":0,"price":120.14502,"timestamp":"2016-01-11T15:11:09+0000","source":"YAHOO","ccy1":"USD"},{"ccy2":"SEK","quoteUnit":0,"price":124.14502,"timestamp":"2016-01-11T15:11:09+0000","source":"YAHOO","ccy1":"USD"}],"APILAYER":[{"ccy2":"KRW","quoteUnit":1,"price":14.234985,"timestamp":"2016-01-11T17:12:13+0000","source":"APILAYER","ccy1":"USD"},{"ccy2":"HUF","quoteUnit":0,"price":1.14502,"timestamp":"2016-01-11T15:11:09+0000","source":"APILAYER","ccy1":"USD"},{"ccy2":"CHF","quoteUnit":0,"price":12.14502,"timestamp":"2016-01-11T15:11:09+0000","source":"APILAYER","ccy1":"USD"},{"ccy2":"SEK","quoteUnit":0,"price":104.14502,"timestamp":"2016-01-11T15:11:09+0000","source":"APILAYER","ccy1":"USD"}],"ABS":[{"ccy2":"KRW","quoteUnit":1,"price":2204.234985,"timestamp":"2016-01-11T17:12:13+0000","source":"ABS","ccy1":"USD"},{"ccy2":"HUF","quoteUnit":0,"price":204.14502,"timestamp":"2016-01-11T15:11:09+0000","source":"ABS","ccy1":"USD"},{"ccy2":"CHF","quoteUnit":0,"price":220.14502,"timestamp":"2016-01-11T15:11:09+0000","source":"ABS","ccy1":"USD"},{"ccy2":"SEK","quoteUnit":0,"price":224.14502,"timestamp":"2016-01-11T15:11:09+0000","source":"ABS","ccy1":"USD"}],"HLC":[{"ccy2":"KRW","quoteUnit":1,"price":24.234985,"timestamp":"2016-01-11T17:12:13+0000","source":"HLC","ccy1":"USD"},{"ccy2":"HUF","quoteUnit":0,"price":42.14502,"timestamp":"2016-01-11T15:11:09+0000","source":"HLC","ccy1":"USD"},{"ccy2":"CHF","quoteUnit":0,"price":10.14502,"timestamp":"2016-01-11T15:11:09+0000","source":"HLC","ccy1":"USD"},{"ccy2":"SEK","quoteUnit":0,"price":1.14502,"timestamp":"2016-01-11T15:11:09+0000","source":"HLC","ccy1":"USD"}]},"errorMessage":""}"""

  test("RestApiEndpoint shoud be able to expose rest services for one currency pair") {
    Get("/getCurrency?ccy1=USD&ccy2=KRW") ~> currenciesRoute ~> check {
      val response = responseAs[String]

      assert(oneCurrencyPair === response)
    }
  }

  test("RestApiEndpoint shoud be able to expose rest services for all currency pairs") {
    Get("/getCurrency?ccy1=ALL") ~> currenciesRoute ~> check {
      val response = responseAs[String]

      assert(allCurrencyPairs === response)
    }
  }
}

class TestPriceEngine extends Actor with GlobalTestData {
  def receive: Receive = {

    case ApplicationStart                           => {}
    case QuotesRefresh(quotes, senderName)          => {}
    case AllQuotesApiRequest                        => { sender ! AllQuotesApiReply(allPrices, "") }
    case QuoteApiRequest(currency, counterCurrency) => { sender ! QuoteApiReply(usdKrwPrices, "") }
  }
}