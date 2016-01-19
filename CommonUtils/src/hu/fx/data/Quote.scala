package hu.fx.data

trait Quote {
  def ccy1: String
  def ccy2: String
  def quoteUnit: Integer
  def price: Double
  def timestamp: String
  def source: String

  override def toString(): String = {
    "Quote: " + this.getClass.getSimpleName + ", " + ccy1 + ", " + ccy2 + ", " + price + ", " + quoteUnit + ", " + timestamp + ", " + source
  }

  override def equals(that: Any): Boolean = that match {
    case q: Quote => {
      val quote = that.asInstanceOf[Quote]
      quote.ccy1.equals(ccy1) && quote.ccy2.equals(ccy2) && quote.source.equals(source) && quote.price.equals(price) && quote.timestamp.equals(timestamp)
    }
    case _ => false
  }

  override def hashCode(): Int = {
    31 * (ccy1.hashCode() + ccy2.hashCode() + source.hashCode() + timestamp.hashCode())
  }

  def withNewPricePrice(price: Double): Quote
}

case class FxQuote(val ccy2: String, val quoteUnit: Integer, val price: Double, val timestamp: String, val source: String)(implicit val ccy1: String = "USD") extends Quote {
  override def withNewPricePrice(price: Double): Quote = {
    new FxQuote(ccy2, quoteUnit, price, timestamp, source)
  }
}

case class PmQuote(val ccy2: String, val quoteUnit: Integer, val price: Double, val timestamp: String, val source: String) extends Quote {
  override def ccy1 = "USD"
  override def withNewPricePrice(price: Double): Quote = {
    new PmQuote(ccy2, quoteUnit, price, timestamp, source)
  }
}

/**
 * This case class is only used for comparison of two quotes from the point of view of currencies.
 * Note: It does not take into account prices.
 */
case class SimpleQuote(val ccy2: String, val source: String)(implicit val ccy1: String = "USD") extends Quote {
  def quoteUnit = 1
  def price = 1
  def timestamp = ""
  def withNewPricePrice(price: Double): Quote = this

  override def equals(that: Any): Boolean = that match {
    case q: Quote => {
      val quote = that.asInstanceOf[Quote]
      quote.ccy1.equals(ccy1) && quote.ccy2.equals(ccy2) && quote.source.equals(source)
    }
    case _ => false
  }
}

case object EmptyQuote extends Quote {
  def ccy1 = throw new IllegalArgumentException
  def ccy2 = throw new IllegalArgumentException
  def quoteUnit = throw new IllegalArgumentException
  def price = throw new IllegalArgumentException
  def timestamp = throw new IllegalArgumentException
  def source = throw new IllegalArgumentException
  def withNewPricePrice(price: Double): Quote = throw new IllegalArgumentException
  
  override def toString() = "Empty quote."
}
