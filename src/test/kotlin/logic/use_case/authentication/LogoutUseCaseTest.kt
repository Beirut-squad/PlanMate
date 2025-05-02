package logic.use_case.authentication

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.logic.repositories.authentication_repository.AuthenticationRepository
import org.example.logic.use_case.authentication.LogoutUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LogoutUseCaseTest {

    private val authenticationRepository: AuthenticationRepository = mockk(relaxed = true)
    private lateinit var logoutUseCase: LogoutUseCase

    @BeforeEach
    fun setup() {
        logoutUseCase = LogoutUseCase(authenticationRepository)
    }

    @Test
    fun `should return failure when repository logout fails`() {
        // Given
        val logoutResult: Result<Unit> = Result.failure(Exception("Logout failed"))
        every { authenticationRepository.logout() } returns logoutResult

        // When
        val result = logoutUseCase.logout()

        // Then
        assertThat(result).isEqualTo(logoutResult)
    }

    @Test
    fun `should return success when repository logout succeeds`() {
        // Given
        val logoutResult: Result<Unit> = Result.success(Unit)
        every { authenticationRepository.logout() } returns logoutResult

        // When
        val result = logoutUseCase.logout()

        // Then
        assertThat(result).isEqualTo(logoutResult)
    }

    @Test
    fun `should call repository logout once`() {
        // Given
        val logoutResult: Result<Unit> = Result.success(Unit)
        every { authenticationRepository.logout() } returns logoutResult

        // When
        logoutUseCase.logout()

        // Then
        verify(exactly = 1) { authenticationRepository.logout() }
    }
}