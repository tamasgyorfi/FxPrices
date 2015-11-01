package hu.fx.service.integration

import hu.fx.service.yahoo.YahooPriceService
import org.scalatest.FlatSpec
import org.scalatest.Matchers
import hu.fx.service.api.FxQuote
import hu.fx.service.api.FxQuote
import hu.fx.service.api.FxQuote
import hu.fx.service.apilayer.ApiLayerPriceService

trait TestData {
  val expectedYahooQuotes = 173
  val expectedApiLayerQuotes = 162

  val apiLayerEndpointForTest = "http://www.apilayer.net/api/live?access_key=268558b467bd337cf8e79bfcf9830378&format=1"

}

/**
 * The number of quotes returned are expected to change extremely rarely.
 * Testing for nr of Quotes is assumed safe here
 * 
 */
class YahooServiceITest extends FlatSpec with Matchers with TestData {

  "Yahoo service" should "provide 173 quotes when parsed" in {
    val result = new YahooPriceService().getMostFreshQuotes()
    result.size should be(expectedYahooQuotes)

    // sampling the data source
    val sampler1 = new FxQuote("HUF", 0, 0, null, "YAHOO")
    val sampler2 = new FxQuote("DKK", 0, 0, null, "YAHOO")
    val sampler3 = new FxQuote("EUR", 0, 0, null, "YAHOO")

    result should contain(sampler1)
    result should contain(sampler2)
    result should contain(sampler3)
  }

  "ApiLayer service" should "privide 162 quotes when parsed" in {
    val result = new ApiLayerPriceService(apiLayerEndpointForTest).getMostFreshQuotes()
    result.size should be(expectedApiLayerQuotes)

    // sampling the data source
    val sampler1 = new FxQuote("HUF", 0, 0, null, "APILAYER")
    val sampler2 = new FxQuote("DKK", 0, 0, null, "APILAYER")
    val sampler3 = new FxQuote("EUR", 0, 0, null, "APILAYER")

    result should contain(sampler1)
    result should contain(sampler2)
    result should contain(sampler3)
  }
} 