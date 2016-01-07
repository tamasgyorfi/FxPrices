package hu.persistence.data.mongo

import hu.fx.data.Quote
import hu.persistence.data.DataPersister
import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.commons.MongoDBObjectBuilder
import com.mongodb.casbah.commons.MongoDBObjectBuilder
import com.mongodb.DBObject

class DatabaseDataPersister extends DataPersister {

  private class DatabaseObjectFactory {

    private val CCY1 = "ccy1"
    private val CCY2 = "ccy2"
    private val PRICE = "price"
    private val QUOTE_UNIT = "quoteUnit"
    private val SOURCE = "source"
    private val TIMESTAMP = "timestamp"

    def getDbObject(quote: Quote): DBObject = {
      val objectBuilder = MongoDBObject.newBuilder

      objectBuilder += CCY1 -> quote.ccy1
      objectBuilder += CCY2 -> quote.ccy2
      objectBuilder += PRICE -> quote.price
      objectBuilder += QUOTE_UNIT -> quote.quoteUnit
      objectBuilder += SOURCE -> quote.source
      objectBuilder += TIMESTAMP -> quote.timestamp

      objectBuilder.result
    }
  }

  def save(quotes: List[Quote]): Unit = ???
}