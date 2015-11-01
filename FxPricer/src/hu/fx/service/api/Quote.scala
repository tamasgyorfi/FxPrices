package hu.fx.service.api

trait Quote {
  def ccy2: String
  def quoteUnit: Integer
  def price: Double
  def timestamp: String
  def source: String

  def ccy1 = "USD"
  override def toString(): String = {
    "Quote: " + this.getClass.getSimpleName + ", " + ccy1 + ", " + ccy2 + ", " + price + ", " + quoteUnit + ", " + timestamp
  }

  override def equals(that: Any): Boolean = that match {
    case that: Quote => {
      val quote = that.asInstanceOf[Quote]
      quote.ccy2.equals(ccy2) && quote.source.equals(source)
    }
    case _ => false
  }

  override def hashCode(): Int = {
    31 * ccy2.hashCode() + source.hashCode() + timestamp.hashCode()
  }
}

case class FxQuote(val ccy2: String, val quoteUnit: Integer, val price: Double, val timestamp: String, val source: String) extends Quote {
}

case class PmQuote(val ccy2: String, val quoteUnit: Integer, val price: Double, val timestamp: String, val source: String) extends Quote {
}

case object EmptyQuote extends Quote {
  def ccy2 = throw new IllegalArgumentException
  def quoteUnit = throw new IllegalArgumentException
  def price = throw new IllegalArgumentException
  def timestamp = throw new IllegalArgumentException
  def source = throw new IllegalArgumentException
}
