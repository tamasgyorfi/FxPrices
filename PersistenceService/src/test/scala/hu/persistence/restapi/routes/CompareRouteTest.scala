package hu.persistence.restapi.routes

import hu.persistence.FongoFixture
import org.scalatest.BeforeAndAfterAll
import spray.testkit.Specs2RouteTest
import hu.persistence.TestData
import spray.routing.HttpService
import org.scalatest.FunSuite
import akka.actor.Props
import hu.persistence.restapi.WorkerActor
import hu.persistence.data.mongo.DatabaseDataExtractor
import akka.actor.ActorRef
import hu.persistence.restapi.ComparisonReply
import hu.persistence.restapi.ComparisonReply
import hu.persistence.QuoteDeserializer
import hu.fx.data.Quote
import hu.persistence.restapi.ComparisonReply
import hu.persistence.restapi.ComparisonReply
import hu.persistence.restapi.ComparisonReply

class CompareRouteTest extends FunSuite with CurrencyComparisonRoute with Specs2RouteTest with HttpService with FongoFixture with TestData with BeforeAndAfterAll {

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

    Get("/compare/YAHOO?q1_ccy1=USD&q1_ccy2=KRW&q2_ccy1=USD&q2_ccy2=CAD&date=2016-01-11") ~> comparisonRoute ~> check {
      val response = responseAs[String]
      assert(listEqual(allUsdKrwYahoo, getResponseAsObject(response).comparison.get.quote1))
      assert(listEqual(usdCadYahooOnCertainDate, getResponseAsObject(response).comparison.get.quote2))
    }
  }

  test("RestApiEndpoint should return null result when no data found for any of the currency pairs") {

    Get("/compare/YAHOO?q1_ccy1=RON&q1_ccy2=HUF&q2_ccy1=SEK&q2_ccy2=NOK&date=2016-01-11") ~> comparisonRoute ~> check {
      val response = responseAs[String]
      assert(ComparisonReply(None, "") === getResponseAsObject(response))
    }
  }

  test("RestApiEndpoint should return known prices when there is data for only one currency pair - ccy1") {

    Get("/compare/YAHOO?q1_ccy1=HUF&q1_ccy2=KRW&q2_ccy1=USD&q2_ccy2=CAD&date=2016-01-11") ~> comparisonRoute ~> check {
      val response = responseAs[String]
      assert(listEqual(Nil, getResponseAsObject(response).comparison.get.quote1))
      assert(listEqual(usdCadYahooOnCertainDate, getResponseAsObject(response).comparison.get.quote2))
    }
  }

  test("RestApiEndpoint should return known prices when there is data for only one currency pair - ccy2") {

    Get("/compare/YAHOO?q1_ccy1=USD&q1_ccy2=KRW&q2_ccy1=HUF&q2_ccy2=CAD&date=2016-01-11") ~> comparisonRoute ~> check {
      val response = responseAs[String]
      assert(listEqual(allUsdKrwYahoo, getResponseAsObject(response).comparison.get.quote1))
      assert(listEqual(Nil, getResponseAsObject(response).comparison.get.quote2))
    }
  }

  test("RestApiEndpoint should return an error message when the date is not parsable") {

    Get("/compare/YAHOO?q1_ccy1=USD&q1_ccy2=KRW&q2_ccy1=USD&q2_ccy2=CAD&date=2016-01-111") ~> comparisonRoute ~> check {
      val response = responseAs[String]
      assert(ComparisonReply(None, "Error: Unable to parse request for USD, KRW, USD, CAD, YAHOO and 2016-01-111") === getResponseAsObject(response))
    }
  }

  def listEqual(list1: List[Quote], list2: List[Quote]) = {
    if (list1.size != list2.size) {
      false
    } else {
      list1.forall(list2.contains(_)) && list2.forall(list1.contains(_))
    }
  }

  def getResponseAsObject(response: String) = {
    QuoteDeserializer.mapper.readValue(response, classOf[ComparisonReply])
  }
}