package logic.use_case.authentication.encryption

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.logic.use_case.authentication.encryption.DecryptPasswordUseCase
import org.example.logic.use_case.authentication.encryption.Encryptor
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DecryptPasswordUseCaseTest {

    private val encryptor: Encryptor = mockk(relaxed = true)
    private lateinit var decryptPasswordUseCase: DecryptPasswordUseCase

    @BeforeEach
    fun setup() {
        decryptPasswordUseCase = DecryptPasswordUseCase(encryptor)
    }

    @Test
    fun `should return decrypted password when decryptor succeeds`() {
        // Given
        val encryptedPassword = "encryptedPassword123"
        val decryptedPassword = Result.success("myPassword123")
        every { encryptor.decodePassword(encryptedPassword) } returns decryptedPassword

        // When
        val result = decryptPasswordUseCase.decryptPassword(encryptedPassword)

        // Then
        assertThat(result).isEqualTo(decryptedPassword)
    }

    @Test
    fun `should return failure result if decryptor throws an exception`() {
        // Given
        val encryptedPassword = "encryptedPassword123"
        val failureException = Exception("Decryption error")
        val decryptionFailure = Result.failure<String>(failureException)
        every { encryptor.decodePassword(encryptedPassword) } returns decryptionFailure

        // When
        val result = decryptPasswordUseCase.decryptPassword(encryptedPassword)

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(failureException)
    }

    @Test
    fun `should call the encryptor's decodePassword method once`() {
        // Given
        val encryptedPassword = "encryptedPassword123"
        val decryptedPassword = Result.success("decryptedPassword123")
        every { encryptor.decodePassword(encryptedPassword) } returns decryptedPassword

        // When
        decryptPasswordUseCase.decryptPassword(encryptedPassword)

        // Then
        verify(exactly = 1) { encryptor.decodePassword(encryptedPassword) }
    }
}