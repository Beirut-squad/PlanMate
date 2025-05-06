package org.example.fake_datasource

import org.example.data.datasource.authentication_data_source.AuthenticationDataSource
import org.example.logic.exceptions.authentication_exceptions.EmailAlreadyExistsException
import org.example.logic.exceptions.authentication_exceptions.NoLoggedInUserException
import org.example.models.Role
import org.example.models.User
import java.util.*

class AuthenticationDataSourceFakeImpl : AuthenticationDataSource {
    private val users = mutableListOf<User>()
    private var currentUser: User? = null

    override fun login(email: String, password: String): Result<User> {
        return users
            .find { it.email == email && it.password == password }
            ?.let {
                currentUser = it
                Result.success(it)
            }
            ?: Result.failure(Exception("User not found"))
    }

    override fun checkEmail(email: String): Result<Unit> {
        return users
            .find { it.email == email }
            ?.let { Result.success(Unit) }
            ?: Result.failure(Exception("User already exists"))
    }

    override fun register(name: String, password: String, email: String): Result<User> {
        return if (checkEmail(email).isSuccess) {
            Result.failure(EmailAlreadyExistsException())
        } else {
            val user = User(
                id = UUID.randomUUID(),
                name = name,
                password = password,
                email = email,
                role = Role.MATE
            )
            users.add(user)
            Result.success(user)
        }
    }

    override fun registerAdmin(name: String, password: String, email: String): Result<User> {
        return if (checkEmail(email).isSuccess) {
            Result.failure(EmailAlreadyExistsException())
        } else {
            val user = User(
                id = UUID.randomUUID(),
                name = name,
                password = password,
                email = email,
                role = Role.ADMIN
            )
            users.add(user)
            Result.success(user)
        }
    }

    override fun logout(): Result<Unit> {
        return currentUser?.let {
            currentUser = null
            Result.success(Unit)
        } ?: Result.failure(NoLoggedInUserException())
    }

    override fun checkIfFirstRegister(): Result<Unit> {
        return if (users.isEmpty()) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Users already exist"))
        }
    }

    override fun getCurrentLoggedInUser(): Result<User?> {
        return Result.success(currentUser)
    }

    override fun getUsers(): Result<List<User>> {
        return Result.success(users)
    }
}

// https://meet.google.com/ida-kuzh-khe