package domain.use_case.authentication

import domain.model.User
import domain.repository.AuthenticationRepository

class GetCurrentUserUseCase(
    private val authenticationRepository: AuthenticationRepository,
) {

    suspend fun getCurrentUser(): User {
        return authenticationRepository.getCurrentLoggedInUser()
    }
}