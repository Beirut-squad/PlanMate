package domain.use_case.authentication

import domain.model.User
import org.example.domain.repository.AuthenticationRepository

class GetAllUsersUseCase(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend fun getUsers(): List<User> {
        return authenticationRepository.getUsers()
    }
}