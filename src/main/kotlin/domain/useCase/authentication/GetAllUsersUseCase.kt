package domain.useCase.authentication

import domain.model.User
import domain.repository.AuthenticationRepository

class GetAllUsersUseCase(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend fun getUsers(): List<User> {
        return authenticationRepository.getUsers()
    }
}