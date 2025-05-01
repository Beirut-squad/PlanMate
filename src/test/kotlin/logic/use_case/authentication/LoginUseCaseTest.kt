package logic.use_case.authentication

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.models.User
import creator_helper.createUserHelper
import org.example.logic.repositories.authentication_repository.AuthenticationRepository
import org.example.logic.use_case.authentication.LoginUseCase
import org.example.logic.use_case.authentication.encryption.EncryptPasswordUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LoginUseCaseTest {

    private val authenticationRepository: AuthenticationRepository = mockk(relaxed = true)
    private val encryptPasswordUseCase: EncryptPasswordUseCase = mockk(relaxed = true)
    private lateinit var loginUseCase: LoginUseCase

    @BeforeEach
    fun setup() {
        loginUseCase = LoginUseCase(authenticationRepository, encryptPasswordUseCase)
    }

    @Test
    fun `should return failure when email does not exist`() {
        // Given
        val email = "nonexistent@example.com"
        val password = "password123"
        val emailCheckResult: Result<Unit> = Result.failure(Exception("Email not registered"))
        every { authenticationRepository.checkEmail(email) } returns emailCheckResult

        // When
        val result = loginUseCase.login(email = email, password = password)

        // Then
        assertThat(result).isEqualTo(emailCheckResult)
    }

    @Test
    fun `should return failure when password encryption fails`() {
        // Given
        val user = createUserHelper()
        val emailCheckResult: Result<Unit> = Result.success(Unit)
        every { authenticationRepository.checkEmail(user.email) } returns emailCheckResult
        val encryptResult: Result<String> = Result.failure(Exception("encryption failed"))
        every { encryptPasswordUseCase.encryptPassword(user.password) } returns encryptResult

        // When
        val result = loginUseCase.login(email = user.email, password = user.password)

        // Then
        assertThat(result).isEqualTo(encryptResult)
    }

    @Test
    fun `should return failure when repository login fails`() {
        // Given
        val user = createUserHelper()
        val emailCheckResult: Result<Unit> = Result.success(Unit)
        every { authenticationRepository.checkEmail(user.email) } returns emailCheckResult
        val encryptedPassword = "encryptedPassword"
        every { encryptPasswordUseCase.encryptPassword(user.password) } returns Result.success(encryptedPassword)
        val loginResult: Result<User> = Result.failure(Exception("Login failed"))
        every { authenticationRepository.login(user.email, encryptedPassword) } returns loginResult

        // When
        val result = loginUseCase.login(email = user.email, password = user.password)

        // Then
        assertThat(result).isEqualTo(loginResult)
    }

    @Test
    fun `should return success when login is successful`() {
        // Given
        val user = createUserHelper()
        val emailCheckResult: Result<Unit> = Result.success(Unit)
        every { authenticationRepository.checkEmail(user.email) } returns emailCheckResult
        val encryptedPassword = "encryptedPassword"
        every { encryptPasswordUseCase.encryptPassword(user.password) } returns Result.success(encryptedPassword)
        val loginResult: Result<User> = Result.success(user)
        every { authenticationRepository.login(user.email, encryptedPassword) } returns loginResult

        // When
        val result = loginUseCase.login(email = user.email, password = user.password)

        // Then
        assertThat(result).isEqualTo(loginResult)
    }

    @Test
    fun `should call checkEmail and encryptPassword before login`() {
        // Given
        val user = createUserHelper()
        val emailCheckResult: Result<Unit> = Result.success(Unit)
        every { authenticationRepository.checkEmail(user.email) } returns emailCheckResult
        val encryptedPassword = "encryptedPassword"
        every { encryptPasswordUseCase.encryptPassword(user.password) } returns Result.success(encryptedPassword)
        every { authenticationRepository.login(user.email, encryptedPassword) } returns Result.success(user)

        // When
        loginUseCase.login(email = user.email, password = user.password)

        // Then
        verify(exactly = 1) { authenticationRepository.checkEmail(user.email) }
        verify(exactly = 1) { encryptPasswordUseCase.encryptPassword(user.password) }
        verify(exactly = 1) { authenticationRepository.login(user.email, encryptedPassword) }
    }
}