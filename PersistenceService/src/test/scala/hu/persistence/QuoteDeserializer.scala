package hu.persistence

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.scala.DefaultScalaModule

import hu.fx.data.FxQuote
import hu.fx.data.Quote

object QuoteDeserializer {

  class MyJsonDeserializer extends JsonDeserializer[Quote] {
    override def deserialize(jp: JsonParser, ctxt: DeserializationContext) = {
      jp.readValueAs(classOf[FxQuote])
    }
  }

  private val comparisonResult = """{"comparison":{"quote1":[{"ccy2":"INR","quoteUnit":1,"price":10.4,"timestamp":"2016-01-13T17:12:13+0000","source":"YAHOO","ccy1":"USD"},{"ccy2":"INR","quoteUnit":1,"price":10.1,"timestamp":"2016-01-13T17:13:13+0000","source":"YAHOO","ccy1":"USD"}],"quote2":[{"ccy2":"HUF","quoteUnit":1,"price":0.465,"timestamp":"2016-01-13T17:12:13+0000","source":"YAHOO","ccy1":"USD"},{"ccy2":"HUF","quoteUnit":1,"price":0.467,"timestamp":"2016-01-13T17:13:13+0000","source":"YAHOO","ccy1":"USD"}]},"errorMessage":""}"""

  private val objectMapper = {
    val mapper = new ObjectMapper
    mapper.registerModule(DefaultScalaModule)

    val sm = new SimpleModule("", new Version(1, 0, 0, null))
    mapper.registerModule(sm.addDeserializer(classOf[Quote], new MyJsonDeserializer()))
    mapper
  }

  def mapper = objectMapper
}