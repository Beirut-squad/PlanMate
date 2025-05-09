package domain.use_case.authentication

import data.csv.model.User
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.use_cases.authentication.encryption.EncryptPassword

class RegisterMateUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val encryptPassword: EncryptPassword
) {
    suspend fun addUser(
        name: String,
        password: String,
        email: String
    ): User {
        val encryptedPassword = encryptPassword.encryptPassword(password)
        return authenticationRepository.register(
            name = name,
            password = encryptedPassword,
            email = email
        )
    }

}