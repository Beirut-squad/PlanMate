package org.example.data.repositories.authentication_repository

import org.example.logic.repositories.authentication_repository.AuthenticationRepository
import org.example.models.User

class AuthenticationRepositoryImpl : AuthenticationRepository {
    override fun login(email: String, password: String): Result<User> {
        TODO("Not yet implemented")
    }

    override fun register(name: String, password: String, email: String): Result<User> {
        TODO("Not yet implemented")
    }

    override fun registerAdmin(name: String, password: String, email: String): Result<User> {
        TODO("Not yet implemented")
    }

    override fun logout(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun checkIfFirstRegister(): Result<Unit> {
        TODO("Not yet implemented")
    }
}