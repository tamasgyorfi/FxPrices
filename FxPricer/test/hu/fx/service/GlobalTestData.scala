package hu.fx.service

import hu.fx.data.FxQuote

trait GlobalTestData {

  val usdKrwPrices = List(
    FxQuote("KRW", 0, 1204.14502, "2016-01-11T15:11:09+0000", "YAHOO"),
    FxQuote("KRW", 1, 1204.234985, "2016-01-11T17:12:13+0000", "APILAYER"),
    FxQuote("KRW", 1, 1204.234985, "2016-01-11T17:12:13+0000", "ABS"),
    FxQuote("KRW", 1, 1204.234985, "2016-01-11T17:12:13+0000", "HLC"))

  val allPrices = Map( "YAHOO" -> List(
    FxQuote("KRW", 0, 1204.14502, "2016-01-11T15:11:09+0000", "YAHOO"),
    FxQuote("HUF", 0, 104.14502, "2016-01-11T15:11:09+0000", "YAHOO"),
    FxQuote("CHF", 0, 120.14502, "2016-01-11T15:11:09+0000", "YAHOO"),
    FxQuote("SEK", 0, 124.14502, "2016-01-11T15:11:09+0000", "YAHOO")),

    "APILAYER" -> List(
    FxQuote("KRW", 1, 14.234985, "2016-01-11T17:12:13+0000", "APILAYER"),
    FxQuote("HUF", 0, 1.14502, "2016-01-11T15:11:09+0000", "APILAYER"),
    FxQuote("CHF", 0, 12.14502, "2016-01-11T15:11:09+0000", "APILAYER"),
    FxQuote("SEK", 0, 104.14502, "2016-01-11T15:11:09+0000", "APILAYER")),

    "ABS" -> List(
    FxQuote("KRW", 1, 2204.234985, "2016-01-11T17:12:13+0000", "ABS"),
    FxQuote("HUF", 0, 204.14502, "2016-01-11T15:11:09+0000", "ABS"),
    FxQuote("CHF", 0, 220.14502, "2016-01-11T15:11:09+0000", "ABS"),
    FxQuote("SEK", 0, 224.14502, "2016-01-11T15:11:09+0000", "ABS")),

    "HLC" -> List(
    FxQuote("KRW", 1, 24.234985, "2016-01-11T17:12:13+0000", "HLC"),
    FxQuote("HUF", 0, 42.14502, "2016-01-11T15:11:09+0000", "HLC"),
    FxQuote("CHF", 0, 10.14502, "2016-01-11T15:11:09+0000", "HLC"),
    FxQuote("SEK", 0, 1.14502, "2016-01-11T15:11:09+0000", "HLC")))

}