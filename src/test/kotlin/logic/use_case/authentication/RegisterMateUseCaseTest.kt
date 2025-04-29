package logic.use_case.authentication

import com.google.common.truth.Truth.assertThat
import creator_helper.createUserHelper
import io.mockk.every
import io.mockk.mockk
import org.example.logic.repositories.authentication_repository.AuthenticationRepository
import org.example.logic.use_case.authentication.RegisterMateUseCase
import org.example.models.User
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RegisterMateUseCaseTest {
    private val authenticationRepository: AuthenticationRepository = mockk(relaxed = true)
    private lateinit var registerMateUseCase: RegisterMateUseCase

    @BeforeEach
    fun setup() {
        registerMateUseCase = RegisterMateUseCase(authenticationRepository)
    }

    @Test
    fun `should return failure when repository can't save mate`() {
        // Given
        val user = createUserHelper()
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
        val repositoryResult: Result<User> = Result.success(user)
        every { authenticationRepository.register(any(), any(), any()) } returns repositoryResult

        // When
        val result = registerMateUseCase.addUser(name = user.name, password = user.password, email = user.email)

        // Then
        assertThat(result).isEqualTo(repositoryResult)
    }
}