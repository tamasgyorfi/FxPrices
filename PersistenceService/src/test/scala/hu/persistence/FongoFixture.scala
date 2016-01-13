package hu.persistence

import com.github.fakemongo.Fongo
import com.mongodb.casbah.MongoCollection
import scala.io.Source
import com.mongodb.util.JSON
import com.mongodb.DBObject

trait FongoFixture {
  private val fongo = new Fongo("Test Database")
  private val db = fongo.getDB("fxprices_test")
  private val collection = db.getCollection("quotes_test")

  val mongoCollection = new MongoCollection(collection)

  def insertTestData() = {
    val dbLines = Source.fromFile("src/test/resources/database.txt").getLines()
    dbLines.map { dbLine => JSON.parse(dbLine).asInstanceOf[DBObject] }.foreach { mongoCollection.insert(_) }
  }
  
  def clearTestData() = {
    mongoCollection.drop()
  }
}