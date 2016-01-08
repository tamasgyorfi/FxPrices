package hu.fx.service.pricing

import java.io.StringWriter

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

import spray.httpx.SprayJsonSupport.sprayJsonMarshaller
import spray.httpx.marshalling.ToResponseMarshallable
import spray.httpx.marshalling.ToResponseMarshallable.isMarshallable
import spray.json.DefaultJsonProtocol
import spray.json.JsArray
import spray.json.JsObject
import spray.json.JsValue
import spray.json.pimpAny
import spray.json.pimpString

object JsonHandler extends DefaultJsonProtocol {
  private object Mapper {
    val objectMapper = {
      val mapper = new ObjectMapper
      mapper.registerModule(DefaultScalaModule)
      mapper
    }
  }

  def responseAsJson(response: Any): ToResponseMarshallable = {
    val writer = new StringWriter
    val unit = Mapper.objectMapper.writeValue(writer, response)
    val json = writer.toString().parseJson
    convertToJsonRepresentation(json)
  }

  def exceptionAsJson(ex: Throwable) = Map("error" -> s"Error while making API call: $ex").toJson.asJsObject
  def unknownErrorAsJson = Map("error" -> s"Unknown error while making API call.").toJson.asJsObject

  private def convertToJsonRepresentation(json: JsValue): ToResponseMarshallable = json match {
    case JsArray(_)  => json.asInstanceOf[JsArray]
    case JsObject(_) => json.asJsObject
    case _           => null
  }

}
