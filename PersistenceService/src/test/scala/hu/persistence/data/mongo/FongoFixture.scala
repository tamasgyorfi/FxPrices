package hu.persistence.data.mongo

import com.github.fakemongo.Fongo
import com.mongodb.casbah.MongoCollection

trait FongoFixture {
  private val fongo = new Fongo("Test Database")
  private val db = fongo.getDB("fxprices_test")
  private val collection = db.getCollection("quotes_test")
  
  val mongoCollection = new MongoCollection(collection)
}