package logic.use_case.authentication

import com.google.common.truth.Truth.assertThat
import creator_helper.createUserHelper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.logic.repositories.authentication_repository.AuthenticationRepository
import org.example.logic.use_cases.authentication.GetCurrentLoggedInUserUseCase
import org.example.models.User
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetCurrentLoggedInUserUseCaseTest {

    private val authenticationRepository: AuthenticationRepository = mockk(relaxed = true)
    private lateinit var getCurrentLoggedInUserUseCase: GetCurrentLoggedInUserUseCase

    @BeforeEach
    fun setup() {
        getCurrentLoggedInUserUseCase = GetCurrentLoggedInUserUseCase(authenticationRepository)
    }

    @Test
    fun `should return the current logged-in user when repository returns a user`() {
        // Given
        val expectedUser: User = createUserHelper(name = "John Doe", email = "john.doe@example.com")
        every { authenticationRepository.getCurrentLoggedInUser() } returns Result.success(expectedUser)

        // When
        val result = getCurrentLoggedInUserUseCase.getCurrentUser()

        // Then
        verify(exactly = 1) { authenticationRepository.getCurrentLoggedInUser() }
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(expectedUser)
    }

    @Test
    fun `should return null when repository returns null for current logged-in user`() {
        // Given
        every { authenticationRepository.getCurrentLoggedInUser() } returns Result.success(null)

        // When
        val result = getCurrentLoggedInUserUseCase.getCurrentUser()

        // Then
        verify(exactly = 1) { authenticationRepository.getCurrentLoggedInUser() }
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isNull()
    }

    @Test
    fun `should return failure when repository fails to fetch the current logged-in user`() {
        // Given
        val expectedError = Exception("No user currently logged in")
        every { authenticationRepository.getCurrentLoggedInUser() } returns Result.failure(expectedError)

        // When
        val result = getCurrentLoggedInUserUseCase.getCurrentUser()

        // Then
        verify(exactly = 1) { authenticationRepository.getCurrentLoggedInUser() }
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(expectedError)
    }

    @Test
    fun `should invoke repository exactly once when fetching the current logged-in user`() {
        // Given
        every { authenticationRepository.getCurrentLoggedInUser() } returns Result.success(null)

        // When
        getCurrentLoggedInUserUseCase.getCurrentUser()

        // Then
        verify(exactly = 1) { authenticationRepository.getCurrentLoggedInUser() }
    }
}