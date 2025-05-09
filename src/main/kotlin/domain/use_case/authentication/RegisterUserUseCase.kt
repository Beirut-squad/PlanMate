package domain.use_case.authentication

import org.example.data.model.User
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.use_cases.authentication.encryption.EncryptPassword

class RegisterUserUseCase(
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
