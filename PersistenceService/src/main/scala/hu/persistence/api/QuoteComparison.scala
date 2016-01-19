package hu.persistence.api

import hu.fx.data.Quote

class QuoteComparison(val quote1: List[Quote], val quote2: List[Quote]) {
  
  override def equals(that: Any): Boolean = {
    if (that.isInstanceOf[QuoteComparison]) {
      val thatComparison = that.asInstanceOf[QuoteComparison]
      
      return this.quote1.equals(thatComparison.quote1) && this.quote2.equals(thatComparison.quote2)
    } else {
      false
    }
  }
}