package org.example.logic.use_case.authentication

import org.example.logic.repositories.authentication_repository.AuthenticationRepository
import org.example.logic.use_case.authentication.encryption.EncryptPasswordUseCase
import org.example.models.User

class LoginUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val encryptPasswordUseCase: EncryptPasswordUseCase
) {

    fun login(
        email: String,
        password: String
    ): Result<User> {
        return authenticationRepository.checkEmail(email = email)
            .fold(
                onSuccess = {
                    authenticateUser(email, password)
                },
                onFailure = {
                    Result.failure(it)
                }
            )
    }

    private fun authenticateUser(email: String, password: String): Result<User> {
        return encryptPasswordUseCase.encryptPassword(password = password)
            .fold(
                onSuccess = { encryptedPassword ->
                    authenticationRepository.login(
                        email = email,
                        password = encryptedPassword
                    )
                },
                onFailure = {
                    Result.failure(it)
                }
            )
    }
}