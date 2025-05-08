package org.example.logic.use_cases.authentication

import org.example.logic.repositories.authentication_repository.AuthenticationRepository
import org.example.logic.use_cases.authentication.encryption.EncryptPassword
import org.example.models.User

class RegisterMateUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val encryptPassword: EncryptPassword
) {
    suspend fun addUser(
        name: String,
        password: String,
        email: String
    ): User {
        val encryptedPassword = encryptPassword.encryptPassword(password)
        return authenticationRepository.register(
            name = name,
            password = encryptedPassword,
            email = email
        )
    }

}