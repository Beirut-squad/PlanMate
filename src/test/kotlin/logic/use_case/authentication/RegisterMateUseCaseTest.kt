package logic.use_case.authentication

import com.google.common.truth.Truth.assertThat
import creator_helper.createUserHelper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.logic.repositories.authentication_repository.AuthenticationRepository
import org.example.logic.use_case.authentication.RegisterMateUseCase
import org.example.logic.use_case.authentication.encryption.EncryptPasswordUseCase
import org.example.models.User
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RegisterMateUseCaseTest {
    private val authenticationRepository: AuthenticationRepository = mockk(relaxed = true)
    private val encryptPasswordUseCase: EncryptPasswordUseCase = mockk(relaxed = true)
    private lateinit var registerMateUseCase: RegisterMateUseCase

    @BeforeEach
    fun setup() {
        registerMateUseCase = RegisterMateUseCase(authenticationRepository, encryptPasswordUseCase)
    }

    @Test
    fun `should return failure when repository can't save mate`() {
        // Given
        val user = createUserHelper()
        val encryptionResult: Result<String> = Result.success(user.password)
        every { encryptPasswordUseCase.encryptPassword(user.password) } returns encryptionResult
        val repositoryResult: Result<User> = Result.failure(Exception())
        every { authenticationRepository.register(any(), any(), any()) } returns repositoryResult

        // When
        val result = registerMateUseCase.addUser(name = user.name, password = user.password, email = user.email)

        // Then
        assertThat(result).isEqualTo(repositoryResult)
    }

    @Test
    fun `should return success when repository can save`() {
        // Given
        val user = createUserHelper()
        val encryptionResult: Result<String> = Result.success(user.password)
        every { encryptPasswordUseCase.encryptPassword(user.password) } returns encryptionResult
        val repositoryResult: Result<User> = Result.success(user)
        every { authenticationRepository.register(any(), any(), any()) } returns repositoryResult

        // When
        val result = registerMateUseCase.addUser(name = user.name, password = user.password, email = user.email)

        // Then
        assertThat(result).isEqualTo(repositoryResult)
    }

    @Test
    fun `should encrypt password before saving it to repository`() {
        // Given
        val user = createUserHelper()
        val encryptionResult: Result<String> = Result.success("encrypted")
        every { encryptPasswordUseCase.encryptPassword(user.password) } returns encryptionResult
        every { authenticationRepository.registerAdmin(any(), any(), any()) } returns Result.success(user)

        // When
        registerMateUseCase.addUser(name = user.name, password = user.password, email = user.email)

        // Then
        verify {
            authenticationRepository.register(
                name = user.name,
                password = encryptionResult.getOrThrow(),
                email = user.email
            )
        }
    }

    @Test
    fun `should return failure if encryption failed`() {
        // Given
        val user = createUserHelper()
        val encryptionResult: Result<String> = Result.failure(Exception())
        every { encryptPasswordUseCase.encryptPassword(user.password) } returns encryptionResult

        // When
        val result = registerMateUseCase.addUser(name = user.name, password = user.password, email = user.email)

        // Then
        assertThat(result).isEqualTo(encryptionResult)
    }
}