package logic.use_case.authentication.encryption

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.logic.use_cases.authentication.encryption.EncryptPassword
import org.example.logic.use_cases.authentication.encryption.Encryptor
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class EncryptPasswordTest {

    private val encryptor: Encryptor = mockk(relaxed = true)
    private lateinit var encryptPassword: EncryptPassword

    @BeforeEach
    fun setup() {
        encryptPassword = EncryptPassword(encryptor)
    }

    @Test
    fun `should return encrypted password when encryptor succeeds`() {
        // Given
        val password = "myPassword123"
        val encryptedPassword = Result.success("encryptedPassword123")
        every { encryptor.encodePassword(password) } returns encryptedPassword

        // When
        val result = encryptPassword.encryptPassword(password)

        // Then
        assertThat(result).isEqualTo(encryptedPassword)
    }

    @Test
    fun `should return failure result if encryptor throws an exception`() {
        // Given
        val password = "myPassword123"
        val failureException = Exception("Encryption error")
        val encryptionFailure = Result.failure<String>(failureException)
        every { encryptor.encodePassword(password) } returns encryptionFailure

        // When
        val result = encryptPassword.encryptPassword(password)

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(failureException)
    }

    @Test
    fun `should call the encryptor's encodePassword method once`() {
        // Given
        val password = "myPassword123"
        val encryptedPassword = Result.success("encryptedPassword123")
        every { encryptor.encodePassword(password) } returns encryptedPassword

        // When
        encryptPassword.encryptPassword(password)

        // Then
        verify(exactly = 1) { encryptor.encodePassword(password) }
    }
}