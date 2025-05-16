package data.repository

import data.datasource.AuthenticationDataSource
import domain.model.User
import domain.repository.AuthenticationRepository

class AuthenticationRepositoryImpl(
    private val authenticationDataSource: AuthenticationDataSource
) : AuthenticationRepository {
    override suspend fun login(
        email: String,
        password: String
    ): User {
        return authenticationDataSource.login(
            email = email,
            password = password
        )
    }

    override suspend fun checkEmail(email: String) {
        authenticationDataSource.isValidEmail(email)
    }

    override suspend fun registerMate(
        name: String,
        password: String,
        email: String
    ): User {
        return authenticationDataSource.registerMate(
            email = email,
            password = password,
            name = name
        )
    }

    override suspend fun registerAdmin(
        name: String,
        password: String,
        email: String
    ): User {
        return authenticationDataSource.registerAdmin(
            email = email,
            password = password,
            name = name
        )
    }

    override suspend fun logout() {
        authenticationDataSource.logout()
    }

    override suspend fun isFirstRegister(): Boolean {
        return authenticationDataSource.isFirstRegister()
    }

    override suspend fun getCurrentUser(): User {
        return authenticationDataSource.getCurrentUser()
    }

    override suspend fun getUsers(): List<User> {
        return authenticationDataSource.getUsers()
    }
}