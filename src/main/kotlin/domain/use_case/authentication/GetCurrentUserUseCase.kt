package domain.use_case.authentication

import domain.model.User
import org.example.domain.repository.AuthenticationRepository

class GetCurrentUserUseCase(
    private val authenticationRepository: AuthenticationRepository,
) {

    suspend fun getCurrentUser(): User {
        return authenticationRepository.getCurrentLoggedInUser()
    }
}