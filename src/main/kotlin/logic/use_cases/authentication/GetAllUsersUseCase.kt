package org.example.logic.use_cases.authentication

import org.example.logic.repositories.authentication_repository.AuthenticationRepository
import org.example.models.Role
import org.example.models.User
import java.util.*

class GetAllUsersUseCase(
    private val authenticationRepository: AuthenticationRepository
) {
    fun getUsers(): Result<List<User>> {
        return runCatching {
            authenticationRepository.getUsers().getOrThrow().filter { it.role == Role.MATE }
        }
    }
}