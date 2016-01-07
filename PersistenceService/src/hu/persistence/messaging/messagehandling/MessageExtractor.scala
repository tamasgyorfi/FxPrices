package hu.persistence.messaging.messagehandling

import javax.jms.Message
import hu.fx.data.Quote

trait MessageExtractor {
  def extract(message: Message): List[Quote]
}