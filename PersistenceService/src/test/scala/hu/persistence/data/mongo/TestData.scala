package hu.persistence.data.mongo

import hu.fx.data.FxQuote
import hu.fx.data.PmQuote
import hu.fx.data.EmptyQuote


trait TestData {
  val validQuotes = List(
    new FxQuote("CHF", 1, 32.2, "", "source"),
    new PmQuote("XAU", 1, 11111.1, "", "source"),
    FxQuote("EUR", 1, 11.99, "", "source")("HUF"))

  val mixedQuotes = List(
    new FxQuote("CHF", 1, 32.2, "", "source"),
    EmptyQuote,
    FxQuote("EUR", 1, 11.99, "", "source")("HUF"))

}