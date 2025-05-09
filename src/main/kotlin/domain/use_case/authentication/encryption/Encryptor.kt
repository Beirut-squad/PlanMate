package org.example.domain.use_cases.authentication.encryption

interface Encryptor {
    suspend fun encodePassword(password: String): String
}