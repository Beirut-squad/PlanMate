package org.example.data.datasource.csv

import domain.exception.authentication.EmailAlreadyExistsException
import domain.exception.authentication.EmailNotFoundException
import domain.exception.authentication.InvalidEmailOrPasswordException
import domain.exception.authentication.NoLoggedInUserException
import domain.exception.authentication.UsersAlreadyExistException
import domain.model.Role
import domain.model.User
import org.example.data.csv.helper.FileName
import org.example.data.csv.reader.CsvReader
import org.example.data.csv.writer.CsvWriter
import org.example.data.datasource.AuthenticationDataSource
import java.util.UUID

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
                csvWriter.writeToFile(listOf(user), FileName.CURRENT_USER_FILE)
            } else {
                csvWriter.writeToFile(emptyList(), FileName.CURRENT_USER_FILE)
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
        return csvReader.read(FileName.CURRENT_USER_FILE)
    }

    private fun addUserToCsv(user: User) {
        val users = readUsersFromCsv() + user
        writeUsersToCsv(users)
    }

    private fun writeUsersToCsv(users: List<User>) {
        csvWriter.writeToFile(users, FileName.REGISTERED_USERS_FILE)
    }
}