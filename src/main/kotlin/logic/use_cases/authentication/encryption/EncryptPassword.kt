package org.example.logic.use_cases.authentication.encryption

class EncryptPassword(
    private val encryptor: Encryptor
) {
    fun encryptPassword(password: String): Result<String> {
        return encryptor.encodePassword(password)
    }
}