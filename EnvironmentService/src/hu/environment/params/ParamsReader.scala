package hu.environment.params

trait ParamsReader {
    def getAllParameters(): Map[String, Map[String, String]]
}