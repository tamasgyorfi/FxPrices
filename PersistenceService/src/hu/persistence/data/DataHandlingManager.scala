package hu.persistence.data

trait DataHandlingManager {
  def getDataExtractor(): DataExtractor
  def getDataPersister(): DataPersister
}