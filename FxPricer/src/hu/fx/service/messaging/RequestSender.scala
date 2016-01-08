package hu.fx.service.messaging

import hu.fx.data.Quote

trait RequestSender {
  def sendRequest(payload: List[Quote]): Boolean
}