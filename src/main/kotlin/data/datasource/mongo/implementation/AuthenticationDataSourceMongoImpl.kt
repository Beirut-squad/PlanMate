package data.datasource.mongo.implementation

import data.datasource.interfaces.AuthenticationDataSource
import data.datasource.mongo.mapper.toDocument
import data.datasource.mongo.mapper.toUser
import data.datasource.mongo.mongo_db_connection.MongoConnection
import domain.model.Role
import domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.Document
import domain.exception.InvalidCredentialsException
import domain.exception.ProjectNotFoundException
import domain.exception.UserNotLoggedInException
import java.util.*

class AuthenticationMongoDataSourceImpl(
    private val mongoConnection: MongoConnection,
) : AuthenticationDataSource {
    override suspend fun login(email: String, password: String): User {
        return withContext(Dispatchers.IO) {
            val filter = Document(EMAIL_FILED, email).append(PASSWORD_FILED, password)
            val document = mongoConnection.users.find(filter).firstOrNull()
                ?: throw InvalidCredentialsException()

            val user = document.toUser()
            saveCurrentUser(user)
            user
        }
    }

    override suspend fun isValidEmail(email: String): Boolean {
        return withContext(Dispatchers.IO) {
            val filter = Document(EMAIL_FILED, email)
            mongoConnection.users.find(filter).firstOrNull() != null
        }
    }

    override suspend fun registerMate(name: String, password: String, email: String): User {
        return withContext(Dispatchers.IO) {
            ifEmailExists(email)
            val newUser = createUser(name, password, email, Role.MATE)
            mongoConnection.users.insertOne(newUser.toDocument())
            saveCurrentUser(newUser)
            newUser
        }
    }

    override suspend fun registerAdmin(name: String, password: String, email: String): User {
        return withContext(Dispatchers.IO) {
            ifEmailExists(email)
            val newAdmin = createUser(name, password, email, Role.ADMIN)
            mongoConnection.users.insertOne(newAdmin.toDocument())
            saveCurrentUser(newAdmin)
            newAdmin
        }
    }

    override suspend fun logout() {
        withContext(Dispatchers.IO) {
//            val currentUser = mongoConnection.currentUser.find().firstOrNull() ?: throw UserNotLoggedInException()
            mongoConnection.currentUser.deleteMany(Document())
        }
    }

    override suspend fun isFirstRegister(): Boolean {
        return withContext(Dispatchers.IO) {
            val count = mongoConnection.users.countDocuments()
            count.toInt() == 0
        }
    }

    override suspend fun getCurrentUser(): User {
        return withContext(Dispatchers.IO) {
            val document = mongoConnection.currentUser.find().firstOrNull()
            document?.toUser() ?: throw UserNotLoggedInException()
        }
    }

    override suspend fun getUsers(): List<User> {
        return withContext(Dispatchers.IO) {
            val userDoc = mongoConnection.users.find().toList()
            userDoc.map {
                it?.toUser() ?: throw ProjectNotFoundException()
            }
        }
    }

    private suspend fun saveCurrentUser(user: User) {
        withContext(Dispatchers.IO) {
            val currentUser = MongoConnection.currentUser
            currentUser.deleteMany(Document())
            currentUser.insertOne(user.toDocument())
        }
    }

    private fun createUser(name: String, password: String, email: String, role: Role): User {
        return User(
            id = UUID.randomUUID(),
            name = name,
            password = password,
            email = email,
            role = role,
            isDeleted = false
        )
    }

    private fun ifEmailExists(email: String):Boolean {
        return  mongoConnection.users.find(Document(EMAIL_FILED, email)).first() != null
    }

    companion object {
        private const val EMAIL_FILED = "email"
        private const val PASSWORD_FILED = "password"
    }
}