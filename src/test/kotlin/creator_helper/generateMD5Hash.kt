package creator_helper

import java.security.MessageDigest

fun generateMD5Hash(password: String): String {
    val md = MessageDigest.getInstance("MD5")
    val hash = md.digest(password.toByteArray())
    return hash.joinToString("") { "%02x".format(it) }
}