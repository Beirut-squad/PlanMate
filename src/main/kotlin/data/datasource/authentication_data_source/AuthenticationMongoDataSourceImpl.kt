package org.example.data.datasource.authentication_data_source
import data.csv.FileName.CURRENT_USER_FILE
import data.mongo_db.MongoConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import logic.exceptions.task_management_exception.AuthenticationInvalidException
import org.bson.Document
import org.example.data.datasource.project_data_source.ProjectDataSource
import org.example.logic.exceptions.authentication_exceptions.InvalidEmailOrPasswordException
import org.example.models.Role
import org.example.models.User
import java.lang.annotation.Documented
import java.util.*

class AuthenticationMongoDataSourceImpl(
    private val mongoConnection: MongoConnection
) : AuthenticationDataSource {
    override suspend fun login(email: String, password: String) {
        withContext(Dispatchers.IO) {
            try {
                val filter = Document("email", email).append("password", password)
                val document = mongoConnection.users.find(filter).firstOrNull()
                    ?: throw AuthenticationInvalidException("Invalid email or password")

                val user = User(
                    id = UUID.fromString(document.getString("id")),
                    name = document.getString("name"),
                    password = document.getString("password"),
                    email = document.getString("email"),
                    role = Role.valueOf(document.getString("role")),
                    isDeleted = document.getBoolean("isDeleted", false)
                )

                saveCurrentUser(user)
            } catch (e: AuthenticationInvalidException) {
                throw e
            } catch (e: Exception) {
                throw AuthenticationInvalidException("Login failed: ${e.message}")
            }
        }
    }
    override suspend fun checkEmail(email: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun register(name: String, password: String, email: String): Result<User> {
        TODO("Not yet implemented")
    }

    override suspend fun registerAdmin(name: String, password: String, email: String): Result<User> {
        TODO("Not yet implemented")
    }

    override suspend fun logout(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun checkIfFirstRegister(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getCurrentLoggedInUser(): Result<User?> {
        TODO("Not yet implemented")
    }


    private suspend fun saveCurrentUser(user: User?) = withContext(Dispatchers.IO) {
        try {
            val currentUser = MongoConnection.currentUser
            currentUser.deleteMany(Document())

            if (user != null) {
                val document = Document("id", user.id.toString())
                    .append("name", user.name)
                    .append("email", user.email)
                    .append("password", user.password)
                    .append("role", user.role.name)
                    .append("isDeleted", user.isDeleted)

                currentUser.insertOne(document)
            }
        } catch (e: Exception) {
            throw Exception("Failed for current user${e.message}")
        }
    }



}