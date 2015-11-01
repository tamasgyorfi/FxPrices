package hu.staticdata.params

trait ParamsReader {
    def getAllParameters(): Map[String, Map[String, String]]
}