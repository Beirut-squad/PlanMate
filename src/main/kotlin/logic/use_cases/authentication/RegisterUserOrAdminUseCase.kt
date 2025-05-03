package org.example.logic.use_cases.authentication

import org.example.logic.repositories.authentication_repository.AuthenticationRepository
import org.example.logic.use_cases.authentication.encryption.EncryptPassword
import org.example.models.User

class RegisterUserOrAdminUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val encryptPassword: EncryptPassword,
    private val registerMateUseCase: RegisterMateUseCase
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
                    registerMateUseCase.addUser(name, password, email)
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
    }
}