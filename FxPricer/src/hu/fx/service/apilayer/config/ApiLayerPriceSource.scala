package hu.fx.service.apilayer.config

import hu.fx.service.ParamsSupplier

trait ApiLayerPriceSource {

  val QUOTE_KEY = "quotes"
  val API_ACCESS = ParamsSupplier.getParam("service.apilayer.prices")
  
  val PROVIDER = "APILAYER"
}