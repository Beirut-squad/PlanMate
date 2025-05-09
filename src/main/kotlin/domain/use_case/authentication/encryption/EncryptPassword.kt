package org.example.domain.use_cases.authentication.encryption

class EncryptPassword(
    private val encryptor: Encryptor
) {
    suspend fun encryptPassword(password: String): String {
        return encryptor.encodePassword(password)
    }
}