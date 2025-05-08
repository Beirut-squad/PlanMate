package org.example.logic.use_cases.authentication

import org.example.logic.repositories.authentication_repository.AuthenticationRepository
import org.example.models.User

class GetCurrentLoggedInUserUseCase(
    private val authenticationRepository: AuthenticationRepository
) {

    suspend fun getCurrentUser(): User? {
        return getCurrentUserFromRepository()
    }

    private suspend fun getCurrentUserFromRepository(): User? {
        return authenticationRepository.getCurrentLoggedInUser()
    }
}