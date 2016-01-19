package hu.persistence

import hu.fx.data.EmptyQuote
import hu.fx.data.FxQuote

trait TestData {
  val validQuotes = List(
    FxQuote("CHF", 1, 32.2, "", "source"),
    FxQuote("XAU", 1, 11111.1, "", "source"),
    FxQuote("EUR", 1, 11.99, "", "source")("HUF"))

  val mixedQuotes = List(
    FxQuote("CHF", 1, 32.2, "", "source"),
    EmptyQuote,
    FxQuote("EUR", 1, 11.99, "", "source")("HUF"))

  val allUsdKrwYahoo = List(
    FxQuote("KRW", 0, 1204.14502, "2016-01-11T15:11:09+0000", "YAHOO"),
    FxQuote("KRW", 0, 6.91, "2016-01-11T15:07:03+0000", "YAHOO"),
    FxQuote("KRW", 0, 22450.0, "2016-01-11T15:07:04+0000", "YAHOO"),
    FxQuote("KRW", 0, 7.99125, "2016-01-11T15:07:10+0000", "YAHOO"),
    FxQuote("KRW", 0, 78.408951, "2016-01-11T15:07:02+0000", "YAHOO"),
    FxQuote("KRW", 0, 20.385, "2016-01-11T15:07:01+0000", "YAHOO"),
    FxQuote("KRW", 0, 6.35, "2016-01-11T15:09:00+0000", "YAHOO"),
    FxQuote("KRW", 0, 2.425, "2016-01-11T15:07:00+0000", "YAHOO"))

  val allUsdKrwApiLayer = List(
    FxQuote("KRW", 1, 1204.234985, "2016-01-11T17:12:13+0000", "APILAYER")("USD"))

  val allUsdCadYahoo = List(
    FxQuote("CAD", 0, 1.4334, "2016-01-11T15:11:27+0000", "YAHOO"),
    FxQuote("CAD", 0, 1.4134, "2016-01-11T15:11:27+0000", "YAHOO"),
    FxQuote("CAD", 0, 1.4133, "2016-01-11T15:11:27+0000", "YAHOO"),
    FxQuote("CAD", 0, 1.334, "2016-01-15T15:11:27+0000", "YAHOO"),
    FxQuote("CAD", 0, 1.434, "2016-01-16T15:11:27+0000", "YAHOO"),
    FxQuote("CAD", 0, 1.414, "2016-01-16T23:59:59+0000", "YAHOO"))

  val usdCadYahooOnCertainDate = List(
    FxQuote("CAD", 0, 1.4334, "2016-01-11T15:11:27+0000", "YAHOO"),
    FxQuote("CAD", 0, 1.4134, "2016-01-11T15:11:27+0000", "YAHOO"),
    FxQuote("CAD", 0, 1.4133, "2016-01-11T15:11:27+0000", "YAHOO"))

}