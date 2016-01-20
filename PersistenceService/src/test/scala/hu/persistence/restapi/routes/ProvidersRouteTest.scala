package hu.persistence.restapi.routes

import org.scalatest.BeforeAndAfterAll
import org.scalatest.WordSpecLike
import hu.persistence.FongoFixture
import hu.persistence.TestData
import spray.routing.HttpService
import spray.testkit.Specs2RouteTest
import hu.persistence.data.mongo.DatabaseDataExtractor
import hu.persistence.QuoteDeserializer
import akka.actor.Props
import akka.actor.ActorSystem
import hu.persistence.restapi.WorkerActor
import hu.persistence.restapi.PriceReply
import akka.actor.ActorRef
import scala.concurrent.duration.DurationInt
import hu.persistence.restapi.ProvidersReply

class ProvidersRouteTest extends WordSpecLike with ProvidersRoute with Specs2RouteTest with HttpService with FongoFixture with TestData with BeforeAndAfterAll {

  implicit def default(implicit system: ActorSystem) = RouteTestTimeout(3 seconds)
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

  "Providers route" should {
    "return all the providers from the database" in {
      Get("/providers") ~> providersRoute ~> check {
        val response = responseAs[String]
        assert(listEqual(List("APILAYER", "YAHOO", "APROVIDER"), QuoteDeserializer.mapper.readValue(response, classOf[ProvidersReply]).providers))
      }
    }
  }

}