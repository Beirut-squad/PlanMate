package org.example.data.datasource.mongo

import data.datasource.mapper.toDocument
import data.datasource.mapper.toUser
import org.example.data.datasource.mongo.mongo_db.MongoConnection
import domain.exception.authentication.EmailAlreadyExistsException
import domain.exception.authentication.EmailNotFoundException
import domain.exception.authentication.InvalidEmailOrPasswordException
import domain.exception.authentication.NoLoggedInUserException
import domain.exception.authentication.UsersAlreadyExistException
import domain.model.Role
import domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.Document
import org.example.data.datasource.AuthenticationDataSource
import java.util.UUID

class AuthenticationDataSourceMongoImpl(
    private val mongoConnection: MongoConnection
) : AuthenticationDataSource {
    override suspend fun login(email: String, password: String): User {
        return withContext(Dispatchers.IO) {
            val filter = Document(EMAIL_FILED, email).append(PASSWORD_FILED, password)
            val document = mongoConnection.users.find(filter).firstOrNull()
                ?: throw InvalidEmailOrPasswordException()

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

    override suspend fun register(name: String, password: String, email: String): User {
        return withContext(Dispatchers.IO) {
            checkIfEmailExists(email)
            val newUser = createUser(name, password, email, Role.MATE)
            mongoConnection.users.insertOne(newUser.toDocument())
            saveCurrentUser(newUser)
            newUser
        }
    }

    override suspend fun registerAdmin(name: String, password: String, email: String): User {
        return withContext(Dispatchers.IO) {
            checkIfEmailExists(email)
            val newAdmin = createUser(name, password, email, Role.ADMIN)
            mongoConnection.users.insertOne(newAdmin.toDocument())
            saveCurrentUser(newAdmin)
            newAdmin
        }
    }

    override suspend fun logout() {
        withContext(Dispatchers.IO) {
            val currentUser = mongoConnection.currentUser.find().firstOrNull()
            if (currentUser == null) throw NoLoggedInUserException()
            mongoConnection.currentUser.deleteMany(Document())
        }
    }

    override suspend fun isFirstRegister(): Boolean {
        return withContext(Dispatchers.IO) {
            val count = mongoConnection.users.countDocuments()
            count.toInt() == 0
        }
    }

    override suspend fun getCurrentUser(): User? {
        return withContext(Dispatchers.IO) {
            val document = mongoConnection.currentUser.find().firstOrNull()
            document?.toUser()
        }
    }

    override suspend fun getUsers(): List<User> {
        return withContext(Dispatchers.IO) {
            val userDoc = mongoConnection.users.find().toList()
            userDoc.map {
                it?.toUser() ?: throw Exception("No any users ")
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

    private fun checkIfEmailExists(email: String) {
        val exists = mongoConnection.users.find(Document(EMAIL_FILED, email)).firstOrNull() != null
        if (exists) throw EmailAlreadyExistsException()
    }

    companion object {
        private const val EMAIL_FILED = "email"
        private const val PASSWORD_FILED = "password"
    }
}