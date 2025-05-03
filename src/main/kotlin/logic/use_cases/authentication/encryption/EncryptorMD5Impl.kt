package org.example.logic.use_cases.authentication.encryption

import java.security.MessageDigest

class EncryptorMD5Impl : Encryptor {
    override fun encodePassword(password: String): Result<String> {
        return try {
            Result.success(makeMD5Hash(password))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun makeMD5Hash(password: String): String {
        val md = MessageDigest.getInstance(MD5_ALGORITHM_NAME)
        val hash = md.digest(password.toByteArray())
        return hash.joinToString("") { "%02x".format(it) }
    }

    companion object {
        private const val MD5_ALGORITHM_NAME = "MD5"
    }
}