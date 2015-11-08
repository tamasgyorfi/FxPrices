package hu.fx.service.api

import akka.actor.Actor

trait PriceSource extends Actor {
  def getMostFreshQuotes: Unit => List[Quote]
}