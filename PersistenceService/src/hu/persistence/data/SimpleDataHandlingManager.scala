package hu.persistence.data

import hu.persistence.api.DataHandlerType
import hu.persistence.data.cache.InMemoryDataExtractor
import hu.persistence.data.cache.InMemoryDataPersister
import hu.persistence.data.logger.LoggingDataExtractor
import hu.persistence.data.logger.LoggingDataPersister
import hu.persistence.data.mongo.DatabaseDataExtractor
import hu.persistence.data.mongo.DatabaseDataPersister

class SimpleDataHandlingManager(persistenceType: DataHandlerType.Value) extends DataHandlingManager {
  
  def getDataExtractor(): DataExtractor = persistenceType match {
    case DataHandlerType.LOGGING   => new LoggingDataExtractor()
    case DataHandlerType.DATABASE  => new DatabaseDataExtractor()
    case DataHandlerType.IN_MEMORY => new InMemoryDataExtractor()
  }
  
  def getDataPersister(): DataPersister = persistenceType match {
    case DataHandlerType.LOGGING   => new LoggingDataPersister()
    case DataHandlerType.DATABASE  => new DatabaseDataPersister()
    case DataHandlerType.IN_MEMORY => new InMemoryDataPersister()    
  }
}

object SimpleDataHandlingManager {
  def apply(persistenceType: DataHandlerType.Value) = {
    new SimpleDataHandlingManager(persistenceType)
  }
}