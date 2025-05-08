package org.example.logic.use_cases.authentication

import org.example.logic.repositories.authentication_repository.AuthenticationRepository
import org.example.logic.use_cases.authentication.encryption.EncryptPassword
import org.example.models.User

class RegisterUserOrAdminUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val encryptPassword: EncryptPassword,
    private val registerMateUseCase: RegisterMateUseCase
) {

    suspend fun add(
        name: String,
        password: String,
        email: String,
    ): User {
        return try {
            authenticationRepository.checkIfFirstRegister()
            val encryptedPassword = encryptPassword.encryptPassword(password)
            authenticationRepository.registerAdmin(
                name = name,
                password = encryptedPassword,
                email = email
            )
        } catch (e: Exception) {
            registerMateUseCase.addUser(name = name, password = password, email = email)
        }
    }
}
