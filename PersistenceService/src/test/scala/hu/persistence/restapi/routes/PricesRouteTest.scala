package hu.persistence.restapi.routes

import scala.concurrent.duration.DurationInt
import org.scalatest.BeforeAndAfterAll
import org.scalatest.WordSpecLike
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import hu.persistence.FongoFixture
import hu.persistence.TestData
import hu.persistence.data.mongo.DatabaseDataExtractor
import hu.persistence.restapi.WorkerActor
import spray.routing.HttpService
import spray.testkit.Specs2RouteTest
import hu.persistence.restapi.PriceReply
import hu.persistence.QuoteDeserializer
import hu.persistence.restapi.PriceReply
import hu.persistence.restapi.PriceReply
import hu.fx.data.FxQuote

class PricesRouteTest extends WordSpecLike with PricesRoute with Specs2RouteTest with HttpService with FongoFixture with TestData with BeforeAndAfterAll {

  implicit def default(implicit system: ActorSystem) = RouteTestTimeout(3 seconds)
  val dataExtractor = new DatabaseDataExtractor(mongoCollection)
  def actorRefFactory = system
  def is = null

  private val ERROR_MESSAGE = "Error: Unable to parse request for USD, KRW and 2001-01-111"
  
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
      Get("/currencies/YAHOO/USD/KRW/aggregate?filter=max&date=2016-01-11") ~> pricesRoute ~> check {
        val response = responseAs[String]
        assert(PriceReply(Some(KRW3), "") === QuoteDeserializer.mapper.readValue(response, classOf[PriceReply]))
      }
    }

    "return null for a currency pair with no error when there is no data on a date" in {
      Get("/currencies/YAHOO/USD/KRW/aggregate?filter=max&date=2001-01-11") ~> pricesRoute ~> check {
        val response = responseAs[String]
        assert(PriceReply(None, "") === QuoteDeserializer.mapper.readValue(response, classOf[PriceReply]))
      }
    }

    "return null for a currency pair with no error when the currency pair is unknown - ccy1" in {
      Get("/currencies/YAHOO/USDDD/KRW/aggregate?filter=max&date=2016-01-11") ~> pricesRoute ~> check {
        val response = responseAs[String]
        assert(PriceReply(None, "") === QuoteDeserializer.mapper.readValue(response, classOf[PriceReply]))
      }
    }

    "return null for a currency pair with no error when the currency pair is unknown - ccy2" in {
      Get("/currencies/YAHOO/USD/KPW/aggregate?filter=max&date=2016-01-11") ~> pricesRoute ~> check {
        val response = responseAs[String]
        assert(PriceReply(None, "") === QuoteDeserializer.mapper.readValue(response, classOf[PriceReply]))
      }
    }

    "return an error message when the date is not parsable" in {
      Get("/currencies/YAHOO/USD/KRW/aggregate?filter=max&date=2001-01-111") ~> pricesRoute ~> check {
        val response = responseAs[String]
        assert(PriceReply(None, ERROR_MESSAGE) === QuoteDeserializer.mapper.readValue(response, classOf[PriceReply]))
      }
    }
  }

  "MinPrice route" should {

    "return the currency pair's lowest value with no error" in {
      Get("/currencies/YAHOO/USD/KRW/aggregate?filter=min&date=2016-01-11") ~> pricesRoute ~> check {
        val response = responseAs[String]
        assert(PriceReply(Some(KRW8), "") === QuoteDeserializer.mapper.readValue(response, classOf[PriceReply]))
      }
    }

    "return null for a currency pair with no error when there is no data on a date" in {
      Get("/currencies/YAHOO/USD/KRW/aggregate?filter=min&date=2001-01-11") ~> pricesRoute ~> check {
        val response = responseAs[String]
        assert(PriceReply(None, "") === QuoteDeserializer.mapper.readValue(response, classOf[PriceReply]))
      }
    }

    "return null for a currency pair with no error when the currency pair is unknown - ccy1" in {
      Get("/currencies/YAHOO/USDDD/KRW/aggregate?filter=min&date=2016-01-11") ~> pricesRoute ~> check {
        val response = responseAs[String]
        assert(PriceReply(None, "") === QuoteDeserializer.mapper.readValue(response, classOf[PriceReply]))
      }
    }

    "return null for a currency pair with no error when the currency pair is unknown - ccy2" in {
      Get("/currencies/YAHOO/USD/KPW/aggregate?filter=min&date=2016-01-11") ~> pricesRoute ~> check {
        val response = responseAs[String]
        assert(PriceReply(None, "") === QuoteDeserializer.mapper.readValue(response, classOf[PriceReply]))
      }
    }

    "return an error message when the date is not parsable" in {
      Get("/currencies/YAHOO/USD/KRW/aggregate?filter=max&date=2001-01-111") ~> pricesRoute ~> check {
        val response = responseAs[String]
        assert(PriceReply(None, ERROR_MESSAGE) === QuoteDeserializer.mapper.readValue(response, classOf[PriceReply]))
      }
    }
  }

  "DailyMeanPrice route" should {

    "return the currency pair's mean value with no error" in {
      Get("/currencies/YAHOO/USD/KRW/aggregate?filter=avg&date=2016-01-11") ~> pricesRoute ~> check {
        val response = responseAs[String]
        assert(PriceReply(Some(FxQuote("KRW", 1, 2972.0769026249995, "2016-01-11", "YAHOO")), "") === QuoteDeserializer.mapper.readValue(response, classOf[PriceReply]))
      }
    }

    "return null for a currency pair with no error when there is no data on a date" in {
      Get("/currencies/YAHOO/USD/KRW/aggregate?filter=avg&date=2001-01-11") ~> pricesRoute ~> check {
        val response = responseAs[String]
        assert(PriceReply(None, "") === QuoteDeserializer.mapper.readValue(response, classOf[PriceReply]))
      }
    }

    "return null for a currency pair with no error when the currency pair is unknown - ccy1" in {
      Get("/currencies/YAHOO/USDDD/KRW/aggregate?filter=avg&date=2016-01-11") ~> pricesRoute ~> check {
        val response = responseAs[String]
        assert(PriceReply(None, "") === QuoteDeserializer.mapper.readValue(response, classOf[PriceReply]))
      }
    }

    "return null for a currency pair with no error when the currency pair is unknown - ccy2" in {
      Get("/currencies/YAHOO/USD/KPW/aggregate?filter=avg&date=2016-01-11") ~> pricesRoute ~> check {
        val response = responseAs[String]
        assert(PriceReply(None, "") === QuoteDeserializer.mapper.readValue(response, classOf[PriceReply]))
      }
    }

    "return an error message when the date is not parsable" in {
      Get("/currencies/YAHOO/USD/KRW/aggregate?filter=avg&date=2001-01-111") ~> pricesRoute ~> check {
        val response = responseAs[String]
        assert(PriceReply(None, ERROR_MESSAGE) === QuoteDeserializer.mapper.readValue(response, classOf[PriceReply]))
      }
    }
  }
}