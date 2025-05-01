package org.example.logic.use_case.authentication.encryption

class DecryptPasswordUseCase(
    private val encryptor: Encryptor
) {
    fun decryptPassword(password: String): Result<String> {
        return encryptor.decodePassword(password)
    }
}