package org.example.logic.use_case.authentication

import org.example.logic.repositories.authentication_repository.AuthenticationRepository
import org.example.logic.use_case.authentication.encryption.EncryptPassword
import org.example.models.User

class RegisterAdminUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val encryptPassword: EncryptPassword
) {

    fun addAdmin(
        name: String,
        password: String,
        email: String,
    ): Result<User> {
        return authenticationRepository.checkIfFirstRegister()
            .fold(
                onSuccess = {
                    saveUserWithEncryptedPassword(password, name, email)
                },
                onFailure = {
                    Result.failure(it)
                }
            )
    }

    private fun saveUserWithEncryptedPassword(password: String, name: String, email: String): Result<User> {
        return encryptPassword.encryptPassword(password = password)
            .fold(
                onSuccess = { encryptedPassword ->
                    addAdminAfterConfirmation(
                        name = name,
                        password = encryptedPassword,
                        email = email
                    )
                },
                onFailure = {
                    Result.failure(it)
                }
            )
    }

    private fun addAdminAfterConfirmation(
        name: String,
        password: String,
        email: String
    ): Result<User> {
        return authenticationRepository.registerAdmin(
            name = name,
            password = password,
            email = email
        )
            .fold(
                onSuccess = {
                    Result.success(it)
                },
                onFailure = {
                    Result.failure(it)
                }
            )
    }
}