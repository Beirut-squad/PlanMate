package domain.use_case.authentication

import domain.model.User
import org.example.core.domain.exception.UserNotFoundException
import org.example.domain.repository.AuthenticationRepository
import java.util.*

class GetUserByIdUseCase(
    private val authenticationRepository: AuthenticationRepository,
) {
    suspend fun getUser(id: UUID): User {
        return authenticationRepository.getUsers().find { user -> id == user.id }
            ?: throw UserNotFoundException()
    }
}