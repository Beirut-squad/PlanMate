package data.datasource.csv

import data.datasource.csv.helper.FileName
import data.datasource.csv.reader.CsvReader
import data.datasource.csv.writer.CsvWriter
import data.datasource.AuthenticationDataSource
import ui.common.exception.EmailAlreadyExistsException
import ui.common.exception.InvalidCredentialsException
import ui.common.exception.UserNotLoggedInException
import domain.model.UserRole
import domain.model.User
import java.util.*

class AuthenticationDataSourceImpl(
    private val csvWriter: CsvWriter<User>,
    private val csvReader: CsvReader<User>,
) : AuthenticationDataSource {

    override suspend fun login(
        email: String,
        password: String
    ): User {
        val users = readUsersFromCsv()
        val user = users.find { it.email == email && it.password == password } ?: throw InvalidCredentialsException()
        saveCurrentUser(user)
        return user
    }

    override suspend fun isValidEmail(
        email: String
    ): Boolean {
        val users = readUsersFromCsv()
        return users.none { it.email == email }
    }

    override suspend fun registerMate(
        name: String,
        password: String,
        email: String
    ): User {
        val users = readUsersFromCsv()
        validateUniqueEmail(users = users, email = email)

        val newUser = User(
            id = UUID.randomUUID(),
            name = name,
            password = password,
            email = email,
            userRole = UserRole.MATE,
            isDeleted = false
        )

        addUserToCsv(newUser)

        saveCurrentUser(newUser)
        return newUser
    }

    override suspend fun registerAdmin(
        name: String,
        password: String,
        email: String
    ): User {
        val users = readUsersFromCsv()
        validateUniqueEmail(users, email)

        val newAdmin = User(
            id = UUID.randomUUID(),
            name = name,
            password = password,
            email = email,
            userRole = UserRole.ADMIN,
            isDeleted = false
        )
        addUserToCsv(newAdmin)
        saveCurrentUser(newAdmin)
        return newAdmin
    }

    private fun validateUniqueEmail(
        users: List<User>,
        email: String
    ) {
        if (users.any { it.email == email }) {
            throw EmailAlreadyExistsException()
        }
    }

    override suspend fun logout() {
        saveCurrentUser(null)
    }

    override suspend fun isFirstRegister(): Boolean {
        return readUsersFromCsv().isEmpty()
    }

    private suspend fun saveCurrentUser(user: User?) {
        if (user != null) {
            csvWriter.writeToFile(listOf(user), FileName.CURRENT_USER_FILE)
        } else {
            csvWriter.writeToFile(emptyList(), FileName.CURRENT_USER_FILE)
        }
    }

    override suspend fun getCurrentUser(): User {
        return readUsersFromCsv()
            .singleOrNull()
            ?: throw UserNotLoggedInException()
    }

    override suspend fun getUsers(): List<User> {
        return readUsersFromCsv()
    }

    private suspend fun readUsersFromCsv(): List<User> {
        return csvReader.read(FileName.CURRENT_USER_FILE)
    }

    private suspend fun addUserToCsv(user: User) {
        val users = readUsersFromCsv() + user
        writeUsersToCsv(users)
    }

    private suspend fun writeUsersToCsv(users: List<User>) {
        csvWriter.writeToFile(
            items = users,
            filePath = FileName.REGISTERED_USERS_FILE
        )
    }
}