package org.example.logic.use_case.authentication

import org.example.logic.repositories.authentication_repository.AuthenticationRepository
import org.example.models.User

class GetCurrentLoggedInUserUseCase(
    private val authenticationRepository: AuthenticationRepository
) {

    fun getCurrentUser(): Result<User?> {
        return getCurrentUserFromRepository()
    }

    private fun getCurrentUserFromRepository(): Result<User?> {
        return authenticationRepository.getCurrentLoggedInUser()
            .fold(
                onSuccess = { user ->
                    Result.success(user)
                },
                onFailure = { exception ->
                    Result.failure(exception)
                }
            )
    }
}