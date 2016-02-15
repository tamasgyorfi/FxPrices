package hu.persistence.restapi.routes

import org.scalatest.BeforeAndAfterAll
import org.scalatest.FunSuite

import com.fasterxml.jackson.databind.ObjectMapper

import akka.actor.ActorRef
import akka.actor.Props
import hu.fx.data.Quote
import hu.persistence.FongoFixture
import hu.persistence.QuoteDeserializer
import hu.persistence.TestData
import hu.persistence.data.mongo.DatabaseDataExtractor
import hu.persistence.restapi.CurrencyHistoryReply
import hu.persistence.restapi.CurrencyHistoryReply
import hu.persistence.restapi.WorkerActor
import spray.httpx.SprayJsonSupport._
import spray.routing.HttpService
import spray.testkit.Specs2RouteTest

class CurrencyHistoryRouteTest extends FunSuite with CurrencyHistoryRoute with Specs2RouteTest with HttpService with FongoFixture with TestData with BeforeAndAfterAll {

  val dataExtractor = new DatabaseDataExtractor(mongoCollection)
  def actorRefFactory = system
  def is = null

  override def beforeAll() {
    insertTestData()
  }

  override def afterAll() {
    clearTestData()
  }

  def newWorker: ActorRef = {
    actorRefFactory.actorOf(Props(new WorkerActor(dataExtractor)))
  }

  test("RestApiEndpoint should return the history of a currency pair on a specific date with no error") {

    Get("/currencies/YAHOO/USD/KRW?date=2016-01-11") ~> currencyHistoryRoute ~> check {
      val response = responseAs[String]
      assert(listEqual(allUsdKrwYahoo, getResponseAsObject(response).quotes))
    }
  }

  test("RestApiEndpoint should return empty list of a currency pair with no error when there is no data on a date") {

    Get("/currencies/YAHOO/USD/KRW?date=2001-01-11") ~> currencyHistoryRoute ~> check {
      val response = responseAs[String]
      assert(CurrencyHistoryReply(Nil, "") === getResponseAsObject(response))
    }
  }

  test("RestApiEndpoint should return empty list of a currency pair with no error when the currency pair is unknown - ccy1") {

    Get("/currencies/YAHOO/USDDD/KRW?date=2016-01-11") ~> currencyHistoryRoute ~> check {
      val response = responseAs[String]
      assert(CurrencyHistoryReply(Nil, "") === getResponseAsObject(response))
    }
  }

  test("RestApiEndpoint should return empty list of a currency pair with no error when the currency pair is unknown - ccy2") {

    Get("/currencies/YAHOO/USD/KPW?date=2016-01-11") ~> currencyHistoryRoute ~> check {
      val response = responseAs[String]
      assert(CurrencyHistoryReply(Nil, "") === getResponseAsObject(response))
    }
  }

  test("RestApiEndpoint should return an error message when the date is not parsable") {

    Get("/currencies/YAHOO/USD/KRW?date=2001-01-111") ~> currencyHistoryRoute ~> check {
      val response = responseAs[String]
      assert(CurrencyHistoryReply(Nil, "Error: Unable to parse request for USD, KRW and 2001-01-111") === getResponseAsObject(response))
    }
  }

  test("RestApiEndpoint should return the history of a currency pair between two dates with no error") {

    Get("/currencies/YAHOO/USD/CAD?from=2016-01-11&to=2016-01-15") ~> currencyHistoryRoute ~> check {
      val response = responseAs[String]
      assert(listEqual(List(CAD1, CAD2, CAD3, CAD4), getResponseAsObject(response).quotes))
    }
  }

  test("RestApiEndpoint should return empty list of a currency pair with no error when there is no data between two dates") {

    Get("/currencies/YAHOO/USD/CAD?from=2011-01-11&to=2011-01-15") ~> currencyHistoryRoute ~> check {
      val response = responseAs[String]
      assert(CurrencyHistoryReply(Nil, "") === getResponseAsObject(response))
    }
  }

  test("RestApiEndpoint should return an error message when the first date is not parsable") {

    Get("/currencies/YAHOO/USD/CAD?from=2016-01-111&to=2016-01-15") ~> currencyHistoryRoute ~> check {
      val response = responseAs[String]
      assert(CurrencyHistoryReply(Nil, "Error: Unable to parse request for USD, CAD, 2016-01-111 and 2016-01-15") === getResponseAsObject(response))
    }
  }

  test("RestApiEndpoint should return an error message when the second date is not parsable") {

    Get("/currencies/YAHOO/USD/CAD?from=2016-01-11&to=2016-01-155") ~> currencyHistoryRoute ~> check {
      val response = responseAs[String]
      assert(CurrencyHistoryReply(Nil, "Error: Unable to parse request for USD, CAD, 2016-01-11 and 2016-01-155") === getResponseAsObject(response))
    }
  }

  def getResponseAsObject(response: String) = {
    QuoteDeserializer.mapper.readValue(response, classOf[CurrencyHistoryReply])
  }
}