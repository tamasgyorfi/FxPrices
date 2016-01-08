package hu.fx.service.price

import akka.actor.Props
import hu.fx.service.price.apilayer.ApiLayerPriceService
import hu.fx.service.price.yahoo.YahooPriceService
import akka.actor.Actor
import scala.concurrent.duration.DurationInt


trait PriceServices {
  def getPriceServices(): List[ServiceRefreshAssoc]
}