package hu.fx.service.config

import java.io.File
import java.io.FileInputStream
import java.util.Properties

object ConfigReader {

  private val CONFIG_PATH = "resources/props_default.properties"
  private val properties = ConfigReader.readProperties()
  
  def getProperty(key: String): Option[String] = {
    val value = properties.getProperty(key)
    if (value == null) None
    else Some(value)
  }

  private object ConfigReader {
    def readProperties() = {
      val props = new Properties
      val loaded = props.load(new FileInputStream(new File(CONFIG_PATH)))
      props
    }
  }
}