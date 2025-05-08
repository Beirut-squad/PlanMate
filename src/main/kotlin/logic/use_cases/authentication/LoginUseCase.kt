package org.example.logic.use_cases.authentication

import org.example.logic.repositories.authentication_repository.AuthenticationRepository
import org.example.logic.use_cases.authentication.encryption.EncryptPassword
import org.example.models.User

class LoginUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val encryptPassword: EncryptPassword
) {

    suspend fun login(
        email: String,
        password: String
    ): User {
        authenticationRepository.checkEmail(email)
        val encryptedPassword = encryptPassword.encryptPassword(password)
        return authenticationRepository.login(
            email = email,
            password = encryptedPassword
        )
    }

}