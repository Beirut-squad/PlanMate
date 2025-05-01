package org.example.logic.use_case.authentication

import org.example.logic.repositories.authentication_repository.AuthenticationRepository
import org.example.logic.use_case.authentication.encryption.DecryptPasswordUseCase
import org.example.models.User

class LoginUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val decryptPasswordUseCase: DecryptPasswordUseCase
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
        return decryptPasswordUseCase.decryptPassword(password = password)
            .fold(
                onSuccess = { decryptedPassword ->
                    authenticationRepository.login(
                        email = email,
                        password = decryptedPassword
                    )
                },
                onFailure = {
                    Result.failure(it)
                }
            )
    }
}