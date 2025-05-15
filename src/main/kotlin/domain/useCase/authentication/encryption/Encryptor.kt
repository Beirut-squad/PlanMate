package domain.useCase.authentication.encryption

interface Encryptor {
    suspend fun encodePassword(password: String): String
}