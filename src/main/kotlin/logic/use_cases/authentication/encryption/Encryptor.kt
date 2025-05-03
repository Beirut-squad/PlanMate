package org.example.logic.use_cases.authentication.encryption

interface Encryptor {
    fun encodePassword(password: String): Result<String>
}