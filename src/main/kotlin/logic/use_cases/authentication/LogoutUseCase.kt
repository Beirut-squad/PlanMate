package org.example.logic.use_cases.authentication

import org.example.logic.repositories.authentication_repository.AuthenticationRepository

class LogoutUseCase(
    private val authenticationRepository: AuthenticationRepository
) {

    suspend fun logout() {
         authenticationRepository.logout()
    }
}