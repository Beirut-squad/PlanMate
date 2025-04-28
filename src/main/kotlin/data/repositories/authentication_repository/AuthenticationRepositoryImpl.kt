package org.example.data.repositories.authentication_repository

import org.example.logic.repositories.authentication_repository.AuthenticationRepository
import org.example.models.User

class AuthenticationRepositoryImpl : AuthenticationRepository {
    override fun login(email: String, password: String) {
        TODO("Not yet implemented")
    }

    override fun register(user: User) {
        TODO("Not yet implemented")
    }

    override fun logout() {
        TODO("Not yet implemented")
    }
}