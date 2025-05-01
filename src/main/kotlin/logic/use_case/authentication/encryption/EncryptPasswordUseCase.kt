package org.example.logic.use_case.authentication.encryption

class EncryptPasswordUseCase(
    private val encryptor: Encryptor
) {
    fun encryptPassword(password: String): Result<String> {
        return encryptor.encodePassword(password)
    }
}