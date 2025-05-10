package domain.use_case.authentication

import domain.exception.handler.ExceptionHandler
import domain.model.User
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.use_cases.authentication.encryption.EncryptPassword

class RegisterMateUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val encryptPassword: EncryptPassword,
    private val exceptionHandler: ExceptionHandler,
) {
    suspend fun addUser(
        name: String,
        password: String,
        email: String
    ): User {
        return exceptionHandler.runSafely {
            val encryptedPassword = encryptPassword.encryptPassword(password)
            authenticationRepository.register(
                name = name,
                password = encryptedPassword,
                email = email
            )
        }.getOrThrow()
    }

}