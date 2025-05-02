package logic.use_case.authentication.encryption

import com.google.common.truth.Truth.assertThat
import creator_helper.generateMD5Hash
import io.mockk.every
import io.mockk.mockkStatic
import org.example.logic.use_case.authentication.encryption.Encryptor
import org.example.logic.use_case.authentication.encryption.EncryptorMD5Impl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.security.MessageDigest

class EncryptorMD5ImplTest {

    private lateinit var encryptorMD5Impl: EncryptorMD5Impl
    private val md5Instance = MessageDigest.getInstance("MD5")

    @BeforeEach
    fun setup() {
        encryptorMD5Impl = EncryptorMD5Impl()

        mockkStatic(MessageDigest::class)
        every { MessageDigest.getInstance("MD5") } returns md5Instance
    }

    @Test
    fun `should return correct MD5 hash for a given password`() {
        // Given
        val password = "password123"
        val expectedHash = generateMD5Hash(password)

        // When
        val result: Result<String> = encryptorMD5Impl.encodePassword(password)

        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(expectedHash)
    }

    @Test
    fun `should return failure if an error occurs during encryption`() {
        // Given
        val password = "password123"
        val exceptionMessage = "Encryption error"
        val brokenEncryptor = object : Encryptor {
            override fun encodePassword(password: String): Result<String> {
                return Result.failure(Exception(exceptionMessage))
            }
        }

        // When
        val result: Result<String> = brokenEncryptor.encodePassword(password)

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(Exception::class.java)
        assertThat(result.exceptionOrNull()?.message).isEqualTo(exceptionMessage)
    }


    @Test
    fun `should generate the same hash for the same password`() {
        // Given
        val password = "password123"

        // When
        val hash1Result: Result<String> = encryptorMD5Impl.encodePassword(password)
        val hash2Result: Result<String> = encryptorMD5Impl.encodePassword(password)

        // Then
        assertThat(hash1Result.isSuccess).isTrue()
        assertThat(hash2Result.isSuccess).isTrue()
        assertThat(hash1Result.getOrNull()).isEqualTo(hash2Result.getOrNull())
    }

    @Test
    fun `should return failure if an error occurs during md5 encryption`() {
        // Given
        val password = "password123"

        mockkStatic(MessageDigest::class)
        every { MessageDigest.getInstance("MD5") } throws RuntimeException("MD5 algorithm not available")

        // When
        val result: Result<String> = encryptorMD5Impl.encodePassword(password)

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(Exception::class.java)
        assertThat(result.exceptionOrNull()?.message).isEqualTo("MD5 algorithm not available")
    }
}