package data.datasource.mongo.mongo_db_connection

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import org.bson.Document
import java.io.FileInputStream
import java.util.Properties

object MongoConnection{

    private val properties = Properties().apply {
        load(FileInputStream("local.properties"))
    }
    private val MONGO_URI = properties.getProperty("mongodb.uri")
        ?: throw IllegalStateException("MongoDB URI not found in local.properties")
    private const val DATABASE_NAME = "PlaneMate"

    private val client: MongoClient = MongoClients.create(MONGO_URI)
    private val database = client.getDatabase(DATABASE_NAME)

    // Collections
    val users: MongoCollection<Document> = database.getCollection("users")
    val projects: MongoCollection<Document> = database.getCollection("projects")
    val tasks: MongoCollection<Document> = database.getCollection("tasks")
    val projectLogs: MongoCollection<Document> = database.getCollection("logsForProject")
    val taskLogs: MongoCollection<Document> = database.getCollection("logsForTask")
    val states: MongoCollection<Document> = database.getCollection("states")
    val currentUser: MongoCollection<Document> = database.getCollection("currentUser")
    init {
        try {
            client.listDatabaseNames()
            println("MongoDB connection successfully ")
        } catch (e: Exception) {
            println("MongoDB connection failed: ${e.message}")
            throw RuntimeException("MongoDB connection failed", e)
        }
    }

}