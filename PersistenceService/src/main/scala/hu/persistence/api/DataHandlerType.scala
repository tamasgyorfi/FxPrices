package hu.persistence.api

object DataHandlerType extends Enumeration {
  type DataHandlerType = Value
  val LOGGING, DATABASE, IN_MEMORY = Value
}