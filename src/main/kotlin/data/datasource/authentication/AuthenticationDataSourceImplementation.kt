package data.datasource.authentication

import data.exception.*
import domain.exception.handler.ExceptionHandler
import org.example.data.csv.helper.FileName.CURRENT_USER_FILE
import org.example.data.csv.helper.FileName.REGISTERED_USERS_FILE
import org.example.data.csv.reader.CsvReader
import org.example.data.csv.writer.CsvWriter
import domain.model.Role
import domain.model.User
import java.util.*

class AuthenticationDataSourceImplementation(
    private val csvWriter: CsvWriter<User>,
    private val csvReader: CsvReader<User>,
    private val exceptionHandler: ExceptionHandler,
) : AuthenticationDataSource {

    override suspend fun login(email: String, password: String): User {
        val users = readUsersFromCsv()
        val user = users.find { it.email == email && it.password == password } ?: throw InvalidCredentialsException()
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
        return exceptionHandler.runSafely {
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
            newAdmin
        }.getOrThrow()
    }

    override suspend fun logout() {
        exceptionHandler.runSafely {
            saveCurrentUser(null)
        }
    }

    override suspend fun checkIfFirstRegister() {
        val users = readUsersFromCsv()
        if (users.isNotEmpty()) {
            throw UsersAlreadyExistException()
        }
    }

    private fun saveCurrentUser(user: User?) {
        if (user != null) {
            csvWriter.writeToFile(listOf(user), CURRENT_USER_FILE)
        } else {
            csvWriter.writeToFile(emptyList(), CURRENT_USER_FILE)
        }
    }

    override suspend fun getCurrentLoggedInUser(): User {
        return readUsersFromCsv().singleOrNull() ?: throw UserNotLoggedInException()
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