package hu.fx.config

object EnvironmentSupplier {

  def getEnvironment(): String = {
    val env = System.getenv("ENV")
    if (env == null) "default"
    else env
  }
}