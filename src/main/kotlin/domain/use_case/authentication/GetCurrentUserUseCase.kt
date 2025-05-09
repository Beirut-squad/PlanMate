package domain.use_case.authentication

import data.csv.model.User
import org.example.domain.repository.AuthenticationRepository

class GetCurrentUserUseCase(
    private val authenticationRepository: AuthenticationRepository
) {

    suspend fun getCurrentUser(): User? {
        return getCurrentUserFromRepository()
    }

    private suspend fun getCurrentUserFromRepository(): User? {
        return authenticationRepository.getCurrentLoggedInUser()
    }
}