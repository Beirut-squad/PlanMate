package data.datasource.authentication

import org.example.data.csv.helper.FileName.CURRENT_USER_FILE
import org.example.data.csv.helper.FileName.REGISTERED_USERS_FILE
import org.example.data.csv.reader.CsvReader
import org.example.data.csv.writer.CsvWriter
import org.example.data.model.Role
import org.example.data.model.User
import domain.exception.authentication.*
import java.util.*

class AuthenticationDataSourceImplementation(
    private val csvWriter: CsvWriter<User>,
    private val csvReader: CsvReader<User>
) : AuthenticationDataSource {

    override suspend fun login(email: String, password: String): User {
        val users = readUsersFromCsv()
        val user = users.find { it.email == email && it.password == password } ?: throw InvalidEmailOrPasswordException()
             saveCurrentUser(user)
               return user

    }

    override suspend fun checkEmail(email: String) {
        val users = readUsersFromCsv()
         if (users.none { it.email == email }) {
         throw EmailNotFoundException()
         }
    }

    override suspend fun register(name: String, password: String, email: String): User {
        val users = readUsersFromCsv()
        if (users.any { it.email == email }) {
            throw EmailAlreadyExistsException()
        }

        val newUser = User(
            id = UUID.randomUUID(),
            name = name,
            password = password,
            email = email,
            role = Role.MATE,
            isDeleted = false
        )

        addUserToCsv(newUser)

        saveCurrentUser(newUser)
        return newUser

    }

    override suspend fun registerAdmin(name: String, password: String, email: String): User {
        val users = readUsersFromCsv()
        if (users.any { it.email == email }) {
            throw EmailAlreadyExistsException()
        }

        val newAdmin = User(
            id = UUID.randomUUID(),
            name = name,
            password = password,
            email = email,
            role = Role.ADMIN,
            isDeleted = false
        )

        addUserToCsv(newAdmin)

        saveCurrentUser(newAdmin)
        return newAdmin

    }

    override suspend fun logout() {
        if (getCurrentLoggedInUser() == null) {
            throw NoLoggedInUserException()
        }
        saveCurrentUser(null)
    }

    override suspend fun checkIfFirstRegister() {
        val users = readUsersFromCsv()
         if (users.isNotEmpty()) {
            throw UsersAlreadyExistException()
        }
    }

    private fun saveCurrentUser(user: User?) {
         try {
            if (user != null) {
                csvWriter.writeToFile(listOf(user), CURRENT_USER_FILE)
            } else {
                csvWriter.writeToFile(emptyList(), CURRENT_USER_FILE)
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getCurrentLoggedInUser(): User? {
            return readUsersFromCsv().singleOrNull()
    }

    override suspend fun getUsers(): List<User> {
        return readUsersFromCsv()
    }

    private fun readUsersFromCsv(): List<User> {
        return csvReader.read(CURRENT_USER_FILE)
    }

    private fun addUserToCsv(user: User) {
        val users = readUsersFromCsv() + user
        writeUsersToCsv(users)
    }

    private fun writeUsersToCsv(users: List<User>) {
        csvWriter.writeToFile(users, REGISTERED_USERS_FILE)
    }
}