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
import org.scalatest.FlatSpec
import org.scalatest.WordSpecLike

class PricesRouteTest extends WordSpecLike with PricesRoute with Specs2RouteTest with HttpService with FongoFixture with TestData with BeforeAndAfterAll {

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

  "MaxPrice route" should {

    "return the currency pair's highest value with no error" in {
      Get("/maxPrice/YAHOO?ccy1=USD&ccy2=KRW&date=2016-01-11") ~> pricesRoute ~> check {
        val response = responseAs[String]
        assert(response === """{"quote":{"ccy2":"KRW","quoteUnit":0,"price":22450.0,"timestamp":"2016-01-11T15:07:04+0000","source":"YAHOO","ccy1":"USD"},"errorMessage":""}""")
      }
    }

    "return null for a currency pair with no error when there is no data on a date" in {
      Get("/maxPrice/YAHOO?ccy1=USD&ccy2=KRW&date=2001-01-11") ~> pricesRoute ~> check {
        val response = responseAs[String]
        assert(response === """{"quote":null,"errorMessage":""}""")
      }
    }

    "return null for a currency pair with no error when the currency pair is unknown - ccy1" in {
      Get("/maxPrice/YAHOO?ccy1=USDDD&ccy2=KRW&date=2016-01-11") ~> pricesRoute ~> check {
        val response = responseAs[String]
        assert(response === """{"quote":null,"errorMessage":""}""")
      }
    }

    "return null for a currency pair with no error when the currency pair is unknown - ccy2" in {
      Get("/maxPrice/YAHOO?ccy1=USD&ccy2=KPW&date=2016-01-11") ~> pricesRoute ~> check {
        val response = responseAs[String]
        assert(response === """{"quote":null,"errorMessage":""}""")
      }
    }

    "return an error message when the date is not parsable" in {
      Get("/maxPrice/YAHOO?ccy1=USD&ccy2=KRW&date=2001-01-111") ~> pricesRoute ~> check {
        val response = responseAs[String]
        assert(response === """{"quote":null,"errorMessage":"Error: Unable to parse request for USD, KRW and 2001-01-111"}""")
      }
    }
  }

  "MinPrice route" should {

    "return the currency pair's lowest value with no error" in {
      Get("/minPrice/YAHOO?ccy1=USD&ccy2=KRW&date=2016-01-11") ~> pricesRoute ~> check {
        val response = responseAs[String]
        assert(response === """{"quote":{"ccy2":"KRW","quoteUnit":0,"price":2.425,"timestamp":"2016-01-11T15:07:00+0000","source":"YAHOO","ccy1":"USD"},"errorMessage":""}""")
      }
    }

    "return null for a currency pair with no error when there is no data on a date" in {
      Get("/minPrice/YAHOO?ccy1=USD&ccy2=KRW&date=2001-01-11") ~> pricesRoute ~> check {
        val response = responseAs[String]
        assert(response === """{"quote":null,"errorMessage":""}""")
      }
    }

    "return null for a currency pair with no error when the currency pair is unknown - ccy1" in {
      Get("/minPrice/YAHOO?ccy1=USDDD&ccy2=KRW&date=2016-01-11") ~> pricesRoute ~> check {
        val response = responseAs[String]
        assert(response === """{"quote":null,"errorMessage":""}""")
      }
    }

    "return null for a currency pair with no error when the currency pair is unknown - ccy2" in {
      Get("/minPrice/YAHOO?ccy1=USD&ccy2=KPW&date=2016-01-11") ~> pricesRoute ~> check {
        val response = responseAs[String]
        assert(response === """{"quote":null,"errorMessage":""}""")
      }
    }

    "return an error message when the date is not parsable" in {
      Get("/minPrice/YAHOO?ccy1=USD&ccy2=KRW&date=2001-01-111") ~> pricesRoute ~> check {
        val response = responseAs[String]
        assert(response === """{"quote":null,"errorMessage":"Error: Unable to parse request for USD, KRW and 2001-01-111"}""")
      }
    }
  }
}