package org.example.data.datasource.authentication_data_source

import data.csv.FileName.CURRENT_USER_FILE
import data.csv.FileName.REGISTERED_USERS_FILE
import org.example.data.csv.CsvReader
import org.example.data.csv.CsvWriter
import org.example.logic.exceptions.authentication_exceptions.*
import org.example.models.Role
import org.example.models.User
import java.util.*

class AuthenticationDataSourceImpl(
    private val csvWriter: CsvWriter<User>,
    private val csvReader: CsvReader<User>
) : AuthenticationDataSource {

    override fun login(email: String, password: String): Result<User> {
        val users = readUsersFromCsv()
        val user = users.find { it.email == email && it.password == password }
        return if (user != null) {
            saveCurrentUser(user)
                .fold(
                    onSuccess = {
                        Result.success(user)
                    },
                    onFailure = {
                        Result.failure(it)
                    }
                )
        } else {
            Result.failure(InvalidEmailOrPasswordException())
        }
    }

    override fun checkEmail(email: String): Result<Unit> {
        val users = readUsersFromCsv()
        return if (users.any { it.email == email }) {
            Result.success(Unit)
        } else {
            Result.failure(EmailNotFoundException())
        }
    }

    override fun register(name: String, password: String, email: String): Result<User> {
        val users = readUsersFromCsv()
        if (users.any { it.email == email }) {
            return Result.failure(EmailAlreadyExistsException())
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
        return saveCurrentUser(newUser)
            .fold(
                onSuccess = {
                    Result.success(newUser)
                },
                onFailure = {
                    Result.failure(it)
                }
            )
    }

    override fun registerAdmin(name: String, password: String, email: String): Result<User> {
        val users = readUsersFromCsv()
        if (users.any { it.email == email }) {
            return Result.failure(EmailAlreadyExistsException())
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
        return saveCurrentUser(newAdmin)
            .fold(
                onSuccess = {
                    Result.success(newAdmin)
                },
                onFailure = {
                    Result.failure(it)
                }
            )
    }

    override fun logout(): Result<Unit> {
        return if (!getCurrentLoggedInUser().isFailure) {
            saveCurrentUser(null)
            Result.success(Unit)
        } else {
            Result.failure(NoLoggedInUserException())
        }
    }

    override fun checkIfFirstRegister(): Result<Unit> {
        val users = readUsersFromCsv()
        return if (users.isEmpty()) {
            Result.success(Unit)
        } else {
            Result.failure(UsersAlreadyExistException())
        }
    }

    private fun saveCurrentUser(user: User?): Result<Unit> {
        return try {
            if (user != null) {
                csvWriter.writeToFile(listOf(user), CURRENT_USER_FILE)
            } else {
                csvWriter.writeToFile(emptyList(), CURRENT_USER_FILE)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getCurrentLoggedInUser(): Result<User?> {
        return try {
            Result.success(readUsersFromCsv().single())
        } catch (e: Exception) {
            Result.failure(e)
        }
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