package hu.environment.params

import scala.io.Source
import java.io.File
import java.nio.file.Paths
import java.nio.file.Path
import org.slf4s.LoggerFactory

class IOParamsReader extends ParamsReader {

  val logger = LoggerFactory.getLogger(this.getClass)

  val CONFIG_FILES_PATH = "resources/envs/"
  val CONFIG_FILES_EXT = ".env"
  val KEY_VALUE_SEPARATOR = "="

  def getAllParameters(): Map[String, Map[String, String]] = {
    val envsFiles = getAllEnvs(new File(CONFIG_FILES_PATH))
    val allParams = getParameters(envsFiles)

    logger debug s"Envrionment params files found $envsFiles"
    logger debug s"Parameters found: $allParams"

    allParams
  }

  private def getAllEnvs(envsDir: File): Array[File] = {
    envsDir.listFiles.filter { file => file.isFile() && file.getName.endsWith(CONFIG_FILES_EXT) }
  }

  def getParameters(files: Array[File]): Map[String, Map[String, String]] = {
    files.map { file =>
      {
        val index = file.getName.lastIndexOf(".")
        val env = file.getName.substring(0, index)
        env -> {
          getLines(file) map { fileLine =>
            {
              val keyVal = fileLine split (KEY_VALUE_SEPARATOR) map (_.trim())
              keyVal(0) -> keyVal(1)
            }
          } toMap
        }
      }
    } toMap
  }

  def getLines(file: File): Iterator[String] = {
    Source fromFile (file) getLines () filter (_.contains(KEY_VALUE_SEPARATOR))
  }
}

object IOParamsReader {
  def apply() = new IOParamsReader()
}