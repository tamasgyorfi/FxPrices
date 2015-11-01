package hu.staticdata.params

class Message

case class ParamsRequest(env: String, params: String) extends Message
case class ParamValues(values: List[(String, String)]) extends Message

case object EnvironmentNotFound extends Message