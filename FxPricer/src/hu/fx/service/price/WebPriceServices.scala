package hu.fx.service.price

import akka.actor.Props
import hu.fx.service.price.apilayer.ApiLayerPriceService
import hu.fx.service.price.yahoo.YahooPriceService
import akka.actor.Actor
import scala.concurrent.duration.DurationInt


trait WebPriceServices extends PriceServices with Actor {
  private val yahooActor = context.actorOf(Props[YahooPriceService], name = "YahooPriceService")
  private val apiLayerActor = context.actorOf(Props(new ApiLayerPriceService("")), name = "ApiLayerPriceService")

  private val actorList: List[ServiceRefreshAssoc] = {
    List(new ServiceRefreshAssoc(yahooActor, 5 seconds))
  }

  def getPriceServices(): List[ServiceRefreshAssoc] = {
    actorList
  }
}