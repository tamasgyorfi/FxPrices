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
      assert(response === """{"comparison":{"quote1":[{"ccy2":"KRW","quoteUnit":0,"price":2.425,"timestamp":"2016-01-11T15:07:00+0000","source":"YAHOO","ccy1":"USD"},{"ccy2":"KRW","quoteUnit":0,"price":20.385,"timestamp":"2016-01-11T15:07:01+0000","source":"YAHOO","ccy1":"USD"},{"ccy2":"KRW","quoteUnit":0,"price":78.408951,"timestamp":"2016-01-11T15:07:02+0000","source":"YAHOO","ccy1":"USD"},{"ccy2":"KRW","quoteUnit":0,"price":6.91,"timestamp":"2016-01-11T15:07:03+0000","source":"YAHOO","ccy1":"USD"},{"ccy2":"KRW","quoteUnit":0,"price":22450.0,"timestamp":"2016-01-11T15:07:04+0000","source":"YAHOO","ccy1":"USD"},{"ccy2":"KRW","quoteUnit":0,"price":7.99125,"timestamp":"2016-01-11T15:07:10+0000","source":"YAHOO","ccy1":"USD"},{"ccy2":"KRW","quoteUnit":0,"price":6.35,"timestamp":"2016-01-11T15:09:00+0000","source":"YAHOO","ccy1":"USD"},{"ccy2":"KRW","quoteUnit":0,"price":1204.14502,"timestamp":"2016-01-11T15:11:09+0000","source":"YAHOO","ccy1":"USD"}],"quote2":[{"ccy2":"CAD","quoteUnit":0,"price":1.4334,"timestamp":"2016-01-11T15:11:27+0000","source":"YAHOO","ccy1":"USD"},{"ccy2":"CAD","quoteUnit":0,"price":1.4134,"timestamp":"2016-01-11T15:11:27+0000","source":"YAHOO","ccy1":"USD"},{"ccy2":"CAD","quoteUnit":0,"price":1.4133,"timestamp":"2016-01-11T15:11:27+0000","source":"YAHOO","ccy1":"USD"}]},"errorMessage":""}""")
    }
  }

  test("RestApiEndpoint should return null result when no data found for any of the currency pairs") {

    Get("/compare/YAHOO?q1_ccy1=RON&q1_ccy2=HUF&q2_ccy1=SEK&q2_ccy2=NOK&date=2016-01-11") ~> comparisonRoute ~> check {
      val response = responseAs[String]
      assert(response === """{"comparison":null,"errorMessage":""}""")
    }
  }

  test("RestApiEndpoint should return known prices when there is data for only one currency pair - ccy1") {

    Get("/compare/YAHOO?q1_ccy1=HUF&q1_ccy2=KRW&q2_ccy1=USD&q2_ccy2=CAD&date=2016-01-11") ~> comparisonRoute ~> check {
      val response = responseAs[String]
      assert(response === """{"comparison":{"quote1":[],"quote2":[{"ccy2":"CAD","quoteUnit":0,"price":1.4334,"timestamp":"2016-01-11T15:11:27+0000","source":"YAHOO","ccy1":"USD"},{"ccy2":"CAD","quoteUnit":0,"price":1.4134,"timestamp":"2016-01-11T15:11:27+0000","source":"YAHOO","ccy1":"USD"},{"ccy2":"CAD","quoteUnit":0,"price":1.4133,"timestamp":"2016-01-11T15:11:27+0000","source":"YAHOO","ccy1":"USD"}]},"errorMessage":""}""")
    }
  }

  test("RestApiEndpoint should return known prices when there is data for only one currency pair - ccy2") {

    Get("/compare/YAHOO?q1_ccy1=USD&q1_ccy2=KRW&q2_ccy1=HUF&q2_ccy2=CAD&date=2016-01-11") ~> comparisonRoute ~> check {
      val response = responseAs[String]
      assert(response === """{"comparison":{"quote1":[{"ccy2":"KRW","quoteUnit":0,"price":2.425,"timestamp":"2016-01-11T15:07:00+0000","source":"YAHOO","ccy1":"USD"},{"ccy2":"KRW","quoteUnit":0,"price":20.385,"timestamp":"2016-01-11T15:07:01+0000","source":"YAHOO","ccy1":"USD"},{"ccy2":"KRW","quoteUnit":0,"price":78.408951,"timestamp":"2016-01-11T15:07:02+0000","source":"YAHOO","ccy1":"USD"},{"ccy2":"KRW","quoteUnit":0,"price":6.91,"timestamp":"2016-01-11T15:07:03+0000","source":"YAHOO","ccy1":"USD"},{"ccy2":"KRW","quoteUnit":0,"price":22450.0,"timestamp":"2016-01-11T15:07:04+0000","source":"YAHOO","ccy1":"USD"},{"ccy2":"KRW","quoteUnit":0,"price":7.99125,"timestamp":"2016-01-11T15:07:10+0000","source":"YAHOO","ccy1":"USD"},{"ccy2":"KRW","quoteUnit":0,"price":6.35,"timestamp":"2016-01-11T15:09:00+0000","source":"YAHOO","ccy1":"USD"},{"ccy2":"KRW","quoteUnit":0,"price":1204.14502,"timestamp":"2016-01-11T15:11:09+0000","source":"YAHOO","ccy1":"USD"}],"quote2":[]},"errorMessage":""}""")
    }
  }

  test("RestApiEndpoint should return an error message when the date is not parsable") {

    Get("/compare/YAHOO?q1_ccy1=USD&q1_ccy2=KRW&q2_ccy1=USD&q2_ccy2=CAD&date=2016-01-111") ~> comparisonRoute ~> check {
      val response = responseAs[String]
      assert(response === """{"comparison":null,"errorMessage":"Error: Unable to parse request for USD, KRW, USD, CAD, YAHOO and 2016-01-111"}""")
    }
  }

}