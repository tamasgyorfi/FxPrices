package hu.persistence.restapi.routes

import org.scalatest.FunSuite
import hu.persistence.FongoFixture
import hu.persistence.data.mongo.DatabaseDataExtractor
import spray.routing.HttpService
import spray.testkit.Specs2RouteTest
import akka.actor.Props
import akka.actor.ActorRef
import org.scalatest.BeforeAndAfterAll
import spray.httpx.SprayJsonSupport._
import spray.json._
import hu.persistence.TestData
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import hu.persistence.restapi.WorkerActor

class RestApiEndpointTest extends FunSuite with CurrencyHistoryRoute with Specs2RouteTest with HttpService with FongoFixture with TestData with BeforeAndAfterAll {

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

  test("RestApiEndpoint should return the history of a currency pair with no error") {

    Get("/currencyPairHistory?ccy1=USD&ccy2=KRW&date=2016-01-11") ~> currencyHistoryRoute ~> check {
      val response = responseAs[String]
      assert(response === """{"quotes":[{"ccy2":"KRW","quoteUnit":0,"price":2.425,"timestamp":"2016-01-11T15:07:00+0000","source":"YAHOO","ccy1":"USD"},{"ccy2":"KRW","quoteUnit":0,"price":20.385,"timestamp":"2016-01-11T15:07:01+0000","source":"YAHOO","ccy1":"USD"},{"ccy2":"KRW","quoteUnit":0,"price":78.408951,"timestamp":"2016-01-11T15:07:02+0000","source":"YAHOO","ccy1":"USD"},{"ccy2":"KRW","quoteUnit":0,"price":6.91,"timestamp":"2016-01-11T15:07:03+0000","source":"YAHOO","ccy1":"USD"},{"ccy2":"KRW","quoteUnit":0,"price":22450.0,"timestamp":"2016-01-11T15:07:04+0000","source":"YAHOO","ccy1":"USD"},{"ccy2":"KRW","quoteUnit":0,"price":7.99125,"timestamp":"2016-01-11T15:07:10+0000","source":"YAHOO","ccy1":"USD"},{"ccy2":"KRW","quoteUnit":0,"price":6.35,"timestamp":"2016-01-11T15:09:00+0000","source":"YAHOO","ccy1":"USD"},{"ccy2":"KRW","quoteUnit":0,"price":1204.14502,"timestamp":"2016-01-11T15:11:09+0000","source":"YAHOO","ccy1":"USD"}],"errorMessage":""}""")
    }
  }

  test("RestApiEndpoint should return empty list of a currency pair with no error when there is no data on a date") {

    Get("/currencyPairHistory?ccy1=USD&ccy2=KRW&date=2001-01-11") ~> currencyHistoryRoute ~> check {
      val response = responseAs[String]
      assert(response === """{"quotes":[],"errorMessage":""}""")
    }
  }

  test("RestApiEndpoint should return empty list of a currency pair with no error when the currency pair is unknown - ccy1") {

    Get("/currencyPairHistory?ccy1=USDDD&ccy2=KRW&date=2016-01-11") ~> currencyHistoryRoute ~> check {
      val response = responseAs[String]
      assert(response === """{"quotes":[],"errorMessage":""}""")
    }
  }

  test("RestApiEndpoint should return empty list of a currency pair with no error when the currency pair is unknown - ccy2") {

    Get("/currencyPairHistory?ccy1=USD&ccy2=KPW&date=2016-01-11") ~> currencyHistoryRoute ~> check {
      val response = responseAs[String]
      assert(response === """{"quotes":[],"errorMessage":""}""")
    }
  }

  test("RestApiEndpoint should return an error message when the date is not parsable") {

    Get("/currencyPairHistory?ccy1=USD&ccy2=KRW&date=2001-01-111") ~> currencyHistoryRoute ~> check {
      val response = responseAs[String]
      assert(response === """{"quotes":[],"errorMessage":"Error: Unable to parse request for USD, KRW and 2001-01-111"}""")
    }
  }

}