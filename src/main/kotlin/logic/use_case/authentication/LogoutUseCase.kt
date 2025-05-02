package org.example.logic.use_case.authentication

import org.example.logic.repositories.authentication_repository.AuthenticationRepository

class LogoutUseCase(
    private val authenticationRepository: AuthenticationRepository
) {

    fun logout(): Result<Unit> {
        return authenticationRepository.logout()
    }
}