package hu.persistence

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

  val allUsdKrw = List(
    FxQuote("KRW", 0, 1204.14502, "2016-01-11T15:11:09+0000", "YAHOO"),
    FxQuote("KRW", 0, 6.91, "2016-01-11T15:07:03+0000", "YAHOO"),
    FxQuote("KRW", 0, 22450.0, "2016-01-11T15:07:04+0000", "YAHOO"),
    FxQuote("KRW", 0, 7.99125, "2016-01-11T15:07:10+0000", "YAHOO"),
    FxQuote("KRW", 0, 78.408951, "2016-01-11T15:07:02+0000", "YAHOO"),
    FxQuote("KRW", 0, 20.385, "2016-01-11T15:07:01+0000", "YAHOO"),
    FxQuote("KRW", 0, 6.35, "2016-01-11T15:09:00+0000", "YAHOO"),
    FxQuote("KRW", 0, 2.425, "2016-01-11T15:07:00+0000", "YAHOO"))

}