package org.example.data.datasource.authentication_data_source

import data.mongo_db.MongoConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.Document
import org.example.logic.exceptions.authentication_exceptions.EmailAlreadyExistsException
import org.example.logic.exceptions.authentication_exceptions.EmailNotFoundException
import org.example.logic.exceptions.authentication_exceptions.InvalidEmailOrPasswordException
import org.example.logic.exceptions.authentication_exceptions.NoLoggedInUserException
import org.example.logic.exceptions.authentication_exceptions.UsersAlreadyExistException
import org.example.models.Project
import org.example.models.Role
import org.example.models.User
import java.util.*

class AuthenticationMongoDataSourceImpl(
    private val mongoConnection: MongoConnection
) : AuthenticationDataSource {
    override suspend fun login(email: String, password: String): User {
        return withContext(Dispatchers.IO) {
            val filter = Document(EMAIL_FILED, email).append(PASSWORD_FILED, password)
            val document = mongoConnection.users.find(filter).firstOrNull()
                ?: throw InvalidEmailOrPasswordException()

            val user = User(
                id = UUID.fromString(document.getString(ID_FILED)),
                name = document.getString(NAME_FILED),
                password = document.getString(PASSWORD_FILED),
                email = document.getString(EMAIL_FILED),
                role = Role.valueOf(document.getString(ROLE_FILED)),
                isDeleted = document.getBoolean(IS_DELETED_FILED, false)
            )

            saveCurrentUser(user)
            user
        }
    }

    override suspend fun checkEmail(email: String) {
        withContext(Dispatchers.IO) {
            val filter = Document(EMAIL_FILED, email)
            val exists = mongoConnection.users.find(filter).firstOrNull() != null
            if (!exists) throw EmailNotFoundException()
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

    override suspend fun checkIfFirstRegister() {
        withContext(Dispatchers.IO) {
            val count = mongoConnection.users.countDocuments()
            if (count > 0) throw UsersAlreadyExistException()
        }
    }

    override suspend fun getCurrentLoggedInUser(): User? {
        return withContext(Dispatchers.IO) {
            val document = mongoConnection.currentUser.find().firstOrNull()
            document?.toUser()
        }
    }

    override suspend fun getUsers(): List<User> {
        return withContext(Dispatchers.IO){
            val userDoc = mongoConnection.users.find().toList()
            userDoc.map {
                it?.toUser() ?: throw Exception("No any users ")
            }
        }
    }

    private suspend fun saveCurrentUser(user: User?) = withContext(Dispatchers.IO) {
        try {
            val currentUser = MongoConnection.currentUser
            currentUser.deleteMany(Document())

            if (user != null) {
                val document = Document(ID_FILED, user.id.toString())
                    .append(NAME_FILED, user.name)
                    .append(EMAIL_FILED, user.email)
                    .append(PASSWORD_FILED, user.password)
                    .append(ROLE_FILED, user.role.name)
                    .append(IS_DELETED_FILED, user.isDeleted)

                currentUser.insertOne(document)
            }
        } catch (e: Exception) {
            throw Exception("Failed for current user${e.message}")
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

    private fun User.toDocument(): Document {
        return Document(ID_FILED, this.id.toString())
            .append(NAME_FILED, this.name)
            .append(EMAIL_FILED, this.email)
            .append(PASSWORD_FILED, this.password)
            .append(ROLE_FILED, this.role.name)
            .append(IS_DELETED_FILED, this.isDeleted)
    }

    private fun Document.toUser(): User {
        return User(
            id = UUID.fromString(this.getString(ID_FILED)),
            name = this.getString(NAME_FILED),
            password = this.getString(PASSWORD_FILED),
            email = this.getString(EMAIL_FILED),
            role = Role.valueOf(this.getString(ROLE_FILED)),
            isDeleted = this.getBoolean(IS_DELETED_FILED, false)
        )
    }

    companion object {
        private const val ID_FILED = "_id"
        private const val NAME_FILED = "name"
        private const val EMAIL_FILED = "email"
        private const val PASSWORD_FILED = "password"
        private const val ROLE_FILED = "role"
        private const val IS_DELETED_FILED = "isDeleted"

    }
}