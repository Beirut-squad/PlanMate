package org.example.data.repository

import org.example.data.datasource.AuthenticationDataSource
import domain.model.User
import org.example.domain.repository.AuthenticationRepository

class AuthenticationRepositoryImpl(
    private val authenticationDataSource: AuthenticationDataSource
) : AuthenticationRepository {
    override suspend fun login(email: String, password: String): User {
        return authenticationDataSource.login(email = email, password = password)
    }

    override suspend fun checkEmail(email: String) {
         authenticationDataSource.checkEmail(email)
    }

    override suspend fun register(name: String, password: String, email: String): User {
        return authenticationDataSource.register(email = email, password = password, name = name)
    }

    override suspend fun registerAdmin(name: String, password: String, email: String): User {
        return authenticationDataSource.registerAdmin(email = email, password = password, name = name)
    }

    override suspend fun logout() {
         authenticationDataSource.logout()
    }

    override suspend fun checkIfFirstRegister() {
         authenticationDataSource.checkIfFirstRegister()
    }

    override suspend fun getCurrentLoggedInUser(): User? {
        return authenticationDataSource.getCurrentLoggedInUser()
    }

    override suspend fun getUsers(): List<User> {
        return authenticationDataSource.getUsers()
    }
}