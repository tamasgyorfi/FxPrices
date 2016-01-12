package hu.persistence.data.mongo

import org.scalatest.FunSuite
import hu.fx.data.FxQuote
import hu.fx.data.PmQuote
import hu.fx.data.EmptyQuote
import org.scalatest.BeforeAndAfter
import hu.fx.data.Quote

class DatabaseDataPersisterTest extends FunSuite with BeforeAndAfter with FongoFixture with TestData{

  
  before {
    mongoCollection.drop()
  }
  
  val sut = new DatabaseDataPersister(mongoCollection) {
    protected override def handleException(ex: Exception, quote: Quote) = {
    }
  }

  test("Data persister should save a list of quotes into the database") {
    assert(0 === mongoCollection.count(), "collection should be empty when starting test")
    sut.save(validQuotes)
    assert(3 === mongoCollection.count(), "should have saved all the quotes")
    mongoCollection.find().map(validQuotes.contains(_))
  }

  test("Should only skip erroneous quote while saving quotes list") {
    assert(0 === mongoCollection.count(), "collection should be empty when starting test")
    sut.save(mixedQuotes)
    assert(2 === mongoCollection.count(), "should have saved two quotes out of three")
    mongoCollection.find().map(mixedQuotes.contains(_))
  }
}