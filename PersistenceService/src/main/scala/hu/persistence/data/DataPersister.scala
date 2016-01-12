package hu.persistence.data

import hu.fx.data.Quote

trait DataPersister {
  def save(quotes: List[Quote]): Unit
}