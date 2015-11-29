package hu.fxprices

import org.scalatest.BeforeAndAfter
import org.scalatest.Matchers
import org.scalatest.WordSpecLike


class SummarizerTest extends WordSpecLike with Matchers with BeforeAndAfter {

  "Summarizer" should {
    "not update when prices haven't changed" in {
      val sut = new Summarizer()
//      val quotes = List(
//        new FxQuote()    
//      )
//      
//      sut.update()
    }
  }
}