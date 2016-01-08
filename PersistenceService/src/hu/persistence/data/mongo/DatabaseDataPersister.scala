package hu.persistence.data.mongo

import hu.fx.data.Quote
import hu.persistence.data.DataPersister
import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.commons.MongoDBObjectBuilder
import com.mongodb.casbah.commons.MongoDBObjectBuilder
import com.mongodb.DBObject
import org.slf4s.LoggerFactory
import hu.monitoring.MonitoringManager

class DatabaseDataPersister extends DataPersister {

  private val logger = LoggerFactory.getLogger(this.getClass)
  
  private object DatabaseObjectFactory {

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

  def save(quotes: List[Quote]): Unit = {
    quotes.foreach { quote => saveQuote(quote) }
  }

  private def saveQuote(quote: Quote) = {
    try {
      val dbObject = DatabaseObjectFactory.getDbObject(quote)
	    MongoConfig.collection.save(dbObject)
    } catch {
      case ex:Exception => {
        val errorMessage = s"Unable to save quote ${quote}. Exception was: ${ex}"
        logger error errorMessage
        MonitoringManager reportError errorMessage
      }
    }
  }
}