package domain.use_case.authentication

import domain.repository.AuthenticationRepository
import org.example.domain.use_cases.authentication.encryption.EncryptPassword

class RegisterUserUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val encryptPassword: EncryptPassword,
) {
    suspend fun registerUser(
        name: String,
        password: String,
        email: String
    ) {
        val encryptedPassword = encryptPassword.encryptPassword(password)
        if (authenticationRepository.isFirstRegister()) {
            authenticationRepository.registerAdmin(
                name = name,
                password = encryptedPassword,
                email = email
            )
        } else {
            authenticationRepository.registerMate(
                name = name,
                password = encryptedPassword,
                email = email
            )
        }
    }
}
