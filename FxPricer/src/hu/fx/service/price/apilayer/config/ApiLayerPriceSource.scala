package hu.fx.service.price.apilayer.config

import hu.fx.service.config.ParamsSupplier

trait ApiLayerPriceSource {

  val QUOTE_KEY = "quotes"
  val API_ACCESS = "http://www.apilayer.net/api/live?access_key=1ae57aa3532c306fe9ca9e451a4c24ad"
  
  val PROVIDER = "APILAYER"
}