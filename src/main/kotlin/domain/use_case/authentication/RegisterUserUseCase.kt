package domain.use_case.authentication

import domain.exception.handler.ExceptionHandler
import domain.model.User
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.use_cases.authentication.encryption.EncryptPassword

class RegisterUserUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val encryptPassword: EncryptPassword,
    private val registerMateUseCase: RegisterMateUseCase,
    private val exceptionHandler: ExceptionHandler,
) {

    suspend fun add(
        name: String,
        password: String,
        email: String,
    ): User {
        return exceptionHandler.runSafely {
            authenticationRepository.checkIfFirstRegister()
            val encryptedPassword = encryptPassword.encryptPassword(password)
            authenticationRepository.registerAdmin(
                name = name,
                password = encryptedPassword,
                email = email
            )
        }.fold(
            onFailure = {
                registerMateUseCase.addUser(name = name, password = password, email = email)
            },
            onSuccess = {
                it
            }
        )
    }
}
