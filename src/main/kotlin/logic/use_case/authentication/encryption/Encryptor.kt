package org.example.logic.use_case.authentication.encryption

interface Encryptor {
    fun encodePassword(password: String): Result<String>
}