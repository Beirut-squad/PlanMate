package org.example.logic.use_case.authentication

import org.example.logic.repositories.authentication_repository.AuthenticationRepository

class LoginUseCase(
    private val authenticationRepository: AuthenticationRepository,
) {
    fun login(email: String, password: String) {

    }
}