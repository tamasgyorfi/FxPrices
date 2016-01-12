package hu.persistence.config

import hu.persistence.messaging.messagehandling.ObjectMessageExtractor

object Config {
  val messageExtractor = new ObjectMessageExtractor()
}