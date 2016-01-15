package hu.persistence.data.mongo

import hu.fx.data.Quote
import hu.persistence.data.DataPersister
import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.commons.MongoDBObjectBuilder
import com.mongodb.DBObject
import org.slf4s.LoggerFactory
import hu.monitoring.MonitoringManager
import com.mongodb.casbah.MongoCollection

class DatabaseDataPersister(collection: MongoCollection) extends DataPersister {

  private val logger = LoggerFactory.getLogger(this.getClass)

  private object DatabaseObjectFactory {

    def getDbObject(quote: Quote): DBObject = {
      val objectBuilder = MongoDBObject.newBuilder

      objectBuilder += DbColumnNames.CCY1 -> quote.ccy1
      objectBuilder += DbColumnNames.CCY2 -> quote.ccy2
      objectBuilder += DbColumnNames.PRODUCT -> quote.getClass.getSimpleName
      objectBuilder += DbColumnNames.PRICE -> quote.price
      objectBuilder += DbColumnNames.QUOTE_UNIT -> quote.quoteUnit
      objectBuilder += DbColumnNames.SOURCE -> quote.source
      objectBuilder += DbColumnNames.TIMESTAMP -> quote.timestamp

      objectBuilder.result
    }
  }

  def save(quotes: List[Quote]): Unit = {
    quotes.foreach { quote => saveQuote(quote) }
  }

  private def saveQuote(quote: Quote) = {
    try {
      val dbObject = DatabaseObjectFactory.getDbObject(quote)
      collection.save(dbObject)
    } catch {
      case ex: Exception => {
        ex.printStackTrace()
        handleException(ex, quote)
      }
    }
  }

  protected def handleException(ex: Exception, quote: Quote) = {
    val errorMessage = s"Unable to save quote ${quote}. Exception was: ${ex}"
    logger error errorMessage
    MonitoringManager reportError errorMessage
  }
}