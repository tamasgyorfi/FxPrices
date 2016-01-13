package hu.persistence.restapi.routes

import org.scalatest.BeforeAndAfterAll
import org.scalatest.FunSuite

import akka.actor.ActorRef
import akka.actor.Props
import hu.persistence.FongoFixture
import hu.persistence.TestData
import hu.persistence.data.mongo.DatabaseDataExtractor
import hu.persistence.restapi.WorkerActor
import spray.routing.HttpService
import spray.testkit.Specs2RouteTest

class HighestPriceRouteTest extends FunSuite with HighestPriceRoute with Specs2RouteTest with HttpService with FongoFixture with TestData with BeforeAndAfterAll {

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

  test("RestApiEndpoint should return the currency pair's highest value with no error") {

    Get("/maxPrice?ccy1=USD&ccy2=KRW&date=2016-01-11") ~> maxPriceRoute ~> check {
      val response = responseAs[String]
      assert(response === """{"quote":{"ccy2":"KRW","quoteUnit":0,"price":22450.0,"timestamp":"2016-01-11T15:07:04+0000","source":"YAHOO","ccy1":"USD"},"errorMessage":""}""")
    }
  }

    test("RestApiEndpoint should return null for a currency pair with no error when there is no data on a date") {

    Get("/maxPrice?ccy1=USD&ccy2=KRW&date=2001-01-11") ~> maxPriceRoute ~> check {
      val response = responseAs[String]
      assert(response === """{"quote":null,"errorMessage":""}""")
    }
  }

  test("RestApiEndpoint should return null for a currency pair with no error when the currency pair is unknown - ccy1") {

    Get("/maxPrice?ccy1=USDDD&ccy2=KRW&date=2016-01-11") ~> maxPriceRoute ~> check {
      val response = responseAs[String]
      assert(response === """{"quote":null,"errorMessage":""}""")
    }
  }

  test("RestApiEndpoint should return null for a currency pair with no error when the currency pair is unknown - ccy2") {

    Get("/maxPrice?ccy1=USD&ccy2=KPW&date=2016-01-11") ~> maxPriceRoute ~> check {
      val response = responseAs[String]
      assert(response === """{"quote":null,"errorMessage":""}""")
    }
  }

  test("RestApiEndpoint should return an error message when the date is not parsable") {

    Get("/maxPrice?ccy1=USD&ccy2=KRW&date=2001-01-111") ~> maxPriceRoute ~> check {
      val response = responseAs[String]
      assert(response === """{"quote":null,"errorMessage":"Error: Unable to parse request for USD, KRW and 2001-01-111"}""")
    }
  }


}