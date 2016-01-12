package hu.persistence.data.cache

import hu.fx.data.Quote
import hu.persistence.data.DataPersister

class InMemoryDataPersister extends DataPersister{
    def save(quotes: List[Quote]): Unit = ???

}