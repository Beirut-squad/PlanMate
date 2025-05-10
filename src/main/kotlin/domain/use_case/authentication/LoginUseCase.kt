package domain.use_case.authentication


import domain.exception.handler.ExceptionHandler
import domain.model.User
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.use_cases.authentication.encryption.EncryptPassword

class LoginUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val encryptPassword: EncryptPassword,
    private val exceptionHandler: ExceptionHandler,
) {
    suspend fun login(
        email: String, password: String
    ): User {
        return exceptionHandler.runSafely {
            authenticationRepository.checkEmail(email)
            val encryptedPassword = encryptPassword.encryptPassword(password)
            authenticationRepository.login(
                email = email, password = encryptedPassword
            )
        }.getOrThrow()
    }

}