package hu.fx.service.api

import akka.actor.Actor
import hu.fx.data.Quote

trait PriceSource extends Actor {
  def getMostFreshQuotes: Unit => List[Quote]
}