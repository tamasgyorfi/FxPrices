package hu.persistence.data.mongo

import com.mongodb.casbah.MongoClient

object MongoConfig {
    private val SERVER = "localhost"
    private val PORT   = 27017
    private val DATABASE = "fxprices"
    private val COLLECTION = "quotes"
    
    val connection = MongoClient(SERVER, PORT)
    val collection = connection(DATABASE)(COLLECTION)
}