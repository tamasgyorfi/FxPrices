package hu.fx.service.price.apilayer.config

import hu.fx.service.config.ParamsSupplier

trait ApiLayerPriceSource {

  val QUOTE_KEY = "quotes"
  val API_ACCESS = ParamsSupplier.getParam("service.apilayer.prices")
  
  val PROVIDER = "APILAYER"
}