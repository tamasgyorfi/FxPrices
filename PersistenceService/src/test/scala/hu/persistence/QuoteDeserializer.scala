package hu.persistence

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import hu.persistence.api.QuoteComparison
import hu.persistence.restapi.ComparisonReply

object QuoteDeserializer {
  private val comparisonResult = """{"comparison":{"quote1":[{"ccy2":"INR","quoteUnit":1,"price":10.4,"timestamp":"2016-01-13T17:12:13+0000","source":"YAHOO","ccy1":"USD"},{"ccy2":"INR","quoteUnit":1,"price":10.1,"timestamp":"2016-01-13T17:13:13+0000","source":"YAHOO","ccy1":"USD"}],"quote2":[{"ccy2":"HUF","quoteUnit":1,"price":0.465,"timestamp":"2016-01-13T17:12:13+0000","source":"YAHOO","ccy1":"USD"},{"ccy2":"HUF","quoteUnit":1,"price":0.467,"timestamp":"2016-01-13T17:13:13+0000","source":"YAHOO","ccy1":"USD"}]},"errorMessage":""}"""

  val objectMapper = {
    val mapper = new ObjectMapper
    mapper.registerModule(DefaultScalaModule)
    mapper
  }

  def main(args: Array[String]): Unit = {
    val x = objectMapper.readValue(comparisonResult, classOf[ComparisonReply])
  }
}