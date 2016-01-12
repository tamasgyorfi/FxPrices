package hu.persistence.data

import hu.persistence.api.DataExtractor

trait DataHandlingManager {
  def getDataExtractor(): DataExtractor
  def getDataPersister(): DataPersister
}