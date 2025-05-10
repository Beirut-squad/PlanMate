package org.example.data.repository

import data.datasource.authentication.AuthenticationDataSource
import domain.model.User
import org.example.domain.repository.AuthenticationRepository

class AuthenticationRepositoryImpl(
    private val authentication: AuthenticationDataSource
) : AuthenticationRepository {
    override suspend fun login(email: String, password: String): User {
        return authentication.login(email = email, password = password)
    }

    override suspend fun checkEmail(email: String) {
         authentication.checkEmail(email)
    }

    override suspend fun register(name: String, password: String, email: String): User {
        return authentication.register(email = email, password = password, name = name)
    }

    override suspend fun registerAdmin(name: String, password: String, email: String): User {
        return authentication.registerAdmin(email = email, password = password, name = name)
    }

    override suspend fun logout() {
         authentication.logout()
    }

    override suspend fun checkIfFirstRegister() {
         authentication.checkIfFirstRegister()
    }

    override suspend fun getCurrentLoggedInUser(): User {
        return authentication.getCurrentLoggedInUser()
    }

    override suspend fun getUsers(): List<User> {
        return authentication.getUsers()
    }
}