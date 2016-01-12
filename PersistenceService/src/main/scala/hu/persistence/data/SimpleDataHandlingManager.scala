package hu.persistence.data

import com.mongodb.casbah.MongoCollection

import hu.persistence.api.DataExtractor
import hu.persistence.api.DataHandlerType
import hu.persistence.data.cache.InMemoryDataExtractor
import hu.persistence.data.cache.InMemoryDataPersister
import hu.persistence.data.logger.LoggingDataExtractor
import hu.persistence.data.logger.LoggingDataPersister
import hu.persistence.data.mongo.DatabaseDataExtractor
import hu.persistence.data.mongo.DatabaseDataPersister

class SimpleDataHandlingManager(persistenceType: DataHandlerType.Value, collection: MongoCollection) extends DataHandlingManager {
  
  def getDataExtractor(): DataExtractor = persistenceType match {
    case DataHandlerType.LOGGING   => new LoggingDataExtractor()
    case DataHandlerType.DATABASE  => new DatabaseDataExtractor(collection)
    case DataHandlerType.IN_MEMORY => new InMemoryDataExtractor()
  }
  
  def getDataPersister(): DataPersister = persistenceType match {
    case DataHandlerType.LOGGING   => new LoggingDataPersister()
    case DataHandlerType.DATABASE  => new DatabaseDataPersister(collection)
    case DataHandlerType.IN_MEMORY => new InMemoryDataPersister()    
  }
}

object SimpleDataHandlingManager {
  def apply(persistenceType: DataHandlerType.Value, collection: MongoCollection) = {
    new SimpleDataHandlingManager(persistenceType, collection)
  }
}