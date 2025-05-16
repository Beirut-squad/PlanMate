package domain.useCase.authentication.encryption

import java.security.MessageDigest

class EncryptorMD5Impl : Encryptor {
    override suspend fun encodePassword(password: String): String = makeMD5Hash(password)


    private fun makeMD5Hash(password: String): String {
        val md = MessageDigest.getInstance(MD5_ALGORITHM_NAME)
        val hash = md.digest(password.toByteArray())
        return hash.joinToString("") { "%02x".format(it) }
    }

    companion object {
        private const val MD5_ALGORITHM_NAME = "MD5"
    }
}