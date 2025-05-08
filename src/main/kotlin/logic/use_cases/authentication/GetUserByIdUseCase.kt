package org.example.logic.use_cases.authentication

import org.example.logic.repositories.authentication_repository.AuthenticationRepository
import org.example.models.User
import java.util.UUID

class GetUserByIdUseCase(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend fun getUser(id: UUID): User? {
        return authenticationRepository.getUsers().find { user -> id == user.id }
    }
}