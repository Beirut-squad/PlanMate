package org.example.logic.use_case.authentication

import org.example.logic.repositories.authentication_repository.AuthenticationRepository
import org.example.models.User

class RegisterMateUseCase(
    private val authenticationRepository: AuthenticationRepository
) {
    fun addUser(
        name: String,
        password: String,
        email: String
    ): Result<User> {
        return authenticationRepository.register(
            name = name,
            password = password,
            email = email
        )
    }
}