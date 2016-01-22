package hu.fx.service.price.yahoo.config

import hu.fx.service.config.ParamsSupplier

trait YahooPriceSource {
  val YAHOO_PRICES = "http://finance.yahoo.com/webservice/v1/symbols/allcurrencies/quote"
  val YAHOO_QUOTES_IDENTIFIER = "resources"
  val YAHOO_RATES_IDENTIFIER = "resource"

  val FX_PATTERN = "[A-Z]{3}/[A-Z]{3}"
  val NAME_ATTRIBUTE = "name"
  val VOLUME_ATTRIBUTE = "volume"
  val PRICE_ATTRIBUTE = "price"
  val TIMESTAMP_ATTRIBUTE = "utctime"
  
  val PROVIDER = "YAHOO"
}