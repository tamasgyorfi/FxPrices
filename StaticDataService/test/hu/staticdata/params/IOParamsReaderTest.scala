package hu.staticdata.params

import org.scalatest.FlatSpec
import org.scalatest.mock.MockitoSugar
import java.io.File
import scala.io.Source
import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.mockito.Mockito._
import org.scalatest.Matchers

class IOParamsReaderTest extends FlatSpec with Matchers with MockitoSugar with BeforeAndAfter {

  "ParamsProvider" should "provide all parameters for all environments" in {

    val dev = mock[File]

    when(dev.getName) thenReturn "dev.env"

    val sut = new FakeIoParamsReader()
    val allParams = sut.getParameters(Array(dev))

    val devParams = allParams.get("dev").get
    devParams get ("a.b") should be(Some("c"))
    devParams get ("a.d") should be(Some("q"))
    devParams get ("a.x") should be(Some("y"))

    allParams get ("uat") should be(None)
  }

  class FakeIoParamsReader extends IOParamsReader {
    override def getLines(file: File) = {
      Iterator("a.b = c", "a.d = q", "a.x = y")
    }
  }
}