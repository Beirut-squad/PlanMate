package org.example.logic.use_case.authentication

import org.example.logic.repositories.authentication_repository.AuthenticationRepository
import org.example.models.User

class AddAdminUseCase(
    private val authenticationRepository: AuthenticationRepository
) {

    fun addAdmin(
        name: String,
        password: String,
        email: String,
    ): Result<User> {
        return authenticationRepository.checkIfFirstRegister()
            .fold(
                onSuccess = {
                    addAdminAfterConfirmation(
                        name = name,
                        password = password,
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