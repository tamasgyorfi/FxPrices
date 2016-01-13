package hu.fx.config

import java.io.File
import java.io.FileInputStream
import java.util.Properties

class ConfigReader(path: String) {

  private val properties = ConfigReader.readProperties()
  
  def getProperty(key: String): Option[String] = {
    val value = properties.getProperty(key)
    if (value == null) None
    else Some(value)
  }

  private object ConfigReader {
    def readProperties() = {
      val props = new Properties
      val loaded = props.load(new FileInputStream(new File(path.format(EnvironmentSupplier.getEnvironment()))))
      props
    }
  }
}