package domain.useCase.authentication

import domain.model.User
import domain.repository.AuthenticationRepository
import domain.useCase.authentication.encryption.EncryptPassword

class LoginUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val encryptPassword: EncryptPassword,
) {
    suspend fun login(
        email: String, password: String
    ): User {
        authenticationRepository.checkEmail(email)
        val encryptedPassword = encryptPassword.encryptPassword(password)
        return authenticationRepository.login(
            email = email, password = encryptedPassword
        )
    }

}