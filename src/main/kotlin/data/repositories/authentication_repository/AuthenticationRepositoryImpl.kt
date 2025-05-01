package org.example.data.repositories.authentication_repository

import org.example.data.datasource.authentication_data_source.AuthenticationDataSource
import org.example.logic.repositories.authentication_repository.AuthenticationRepository
import org.example.models.User

class AuthenticationRepositoryImpl(
    private val authenticationDataSource: AuthenticationDataSource
) : AuthenticationRepository {
    override fun login(email: String, password: String): Result<User> {
        return authenticationDataSource.login(email = email, password = password)
    }

    override fun checkEmail(email: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun register(name: String, password: String, email: String): Result<User> {
        return authenticationDataSource.register(email = email, password = password, name = name)
    }

    override fun registerAdmin(name: String, password: String, email: String): Result<User> {
        return authenticationDataSource.registerAdmin(email = email, password = password, name = name)
    }

    override fun logout(): Result<Unit> {
        return authenticationDataSource.logout()
    }

    override fun checkIfFirstRegister(): Result<Unit> {
        return authenticationDataSource.checkIfFirstRegister()
    }
}