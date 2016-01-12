package hu.persistence.config

import com.mongodb.casbah.MongoClient
import com.mongodb.casbah.MongoCollection

trait DbConfig {
    def connection: MongoClient
    def collection: MongoCollection  
}

object MongoConfig extends DbConfig {
    private val SERVER = "localhost"
    private val PORT   = 27017
    private val DATABASE = "fxprices"
    private val COLLECTION = "quotes"
    
    val connection = MongoClient(SERVER, PORT)
    val collection = connection(DATABASE)(COLLECTION)
}