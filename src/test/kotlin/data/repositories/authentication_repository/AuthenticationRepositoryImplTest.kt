package data.repositories.authentication_repository

import com.google.common.truth.Truth.assertThat
import creator_helper.createUserHelper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.data.datasource.authentication_data_source.AuthenticationDataSource
import org.example.data.repositories.authentication_repository.AuthenticationRepositoryImpl
import org.example.logic.repositories.authentication_repository.AuthenticationRepository
import org.example.models.User
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AuthenticationRepositoryImplTest {
    private val authenticationDataSource: AuthenticationDataSource = mockk(relaxed = true)
    private lateinit var authenticationRepository: AuthenticationRepository

    @BeforeEach
    fun setup() {
        authenticationRepository = AuthenticationRepositoryImpl(authenticationDataSource)
    }

    @Test
    fun `should call data source login when repository login`() {
        // Given
        val user = createUserHelper()
        val sourceResult = Result.success(user)
        every { authenticationDataSource.login(any(), any()) } returns sourceResult

        // When
        authenticationRepository.login(email = user.email, password = user.password)

        // Then
        verify { authenticationDataSource.login(email = user.email, password = user.password) }
    }

    @Test
    fun `should return the user given from data source when login success`() {
        // Given
        val user = createUserHelper()
        val sourceResult = Result.success(user)
        every { authenticationDataSource.login(any(), any()) } returns sourceResult

        // When
        val result = authenticationRepository.login(email = user.email, password = user.password)

        // Then
        assertThat(result).isEqualTo(sourceResult)
    }

    @Test
    fun `should return failure when data source login returns failure`() {
        // Given
        val user = createUserHelper()
        val sourceResult = Result.failure<User>(Exception())
        every { authenticationDataSource.login(any(), any()) } returns sourceResult

        // When
        val result = authenticationRepository.login(email = user.email, password = user.password)

        // Then
        assertThat(result).isEqualTo(sourceResult)
    }

    @Test
    fun `should call data source register when repository register`() {
        // Given
        val user = createUserHelper()
        val sourceResult = Result.success(user)
        every { authenticationDataSource.register(any(), any(), any()) } returns sourceResult

        // When
        authenticationRepository.register(email = user.email, password = user.password, name = user.name)

        // Then
        verify { authenticationDataSource.register(email = user.email, password = user.password, name = user.name) }
    }

    @Test
    fun `should return the user given from data source when register success`() {
        // Given
        val user = createUserHelper()
        val sourceResult = Result.success(user)
        every { authenticationDataSource.register(any(), any(), any()) } returns sourceResult

        // When
        val result = authenticationRepository.register(email = user.email, password = user.password, name = user.name)

        // Then
        assertThat(result).isEqualTo(sourceResult)
    }

    @Test
    fun `should return failure when data source register returns failure`() {
        // Given
        val user = createUserHelper()
        val sourceResult = Result.failure<User>(Exception())
        every { authenticationDataSource.register(any(), any(), any()) } returns sourceResult

        // When
        val result = authenticationRepository.register(email = user.email, password = user.password, name = user.name)

        // Then
        assertThat(result).isEqualTo(sourceResult)
    }

    @Test
    fun `should call data source register admin when repository register admin`() {
        // Given
        val user = createUserHelper()
        val sourceResult = Result.success(user)
        every { authenticationDataSource.registerAdmin(any(), any(), any()) } returns sourceResult

        // When
        authenticationRepository.registerAdmin(email = user.email, password = user.password, name = user.name)

        // Then
        verify {
            authenticationDataSource.registerAdmin(
                email = user.email,
                password = user.password,
                name = user.name
            )
        }
    }

    @Test
    fun `should return the user given from data source when register admin success`() {
        // Given
        val user = createUserHelper()
        val sourceResult = Result.success(user)
        every { authenticationDataSource.registerAdmin(any(), any(), any()) } returns sourceResult

        // When
        val result =
            authenticationRepository.registerAdmin(email = user.email, password = user.password, name = user.name)

        // Then
        assertThat(result).isEqualTo(sourceResult)
    }

    @Test
    fun `should return failure when data source register admin returns failure`() {
        // Given
        val user = createUserHelper()
        val sourceResult = Result.failure<User>(Exception())
        every { authenticationDataSource.register(any(), any(), any()) } returns sourceResult

        // When
        val result = authenticationRepository.register(email = user.email, password = user.password, name = user.name)

        // Then
        assertThat(result).isEqualTo(sourceResult)
    }

    @Test
    fun `should call data source logout when repository logout`() {
        // Given
        val sourceResult = Result.success(Unit)
        every { authenticationDataSource.logout() } returns sourceResult

        // When
        authenticationRepository.logout()

        // Then
        verify { authenticationDataSource.logout() }
    }

    @Test
    fun `should return failure from repository logout when data source logout returns failure`() {
        // Given
        val sourceResult = Result.failure<Unit>(Exception())
        every { authenticationDataSource.logout() } returns sourceResult

        // When
        val result = authenticationRepository.logout()

        // Then
        assertThat(result).isEqualTo(sourceResult)
    }

    @Test
    fun `should return success from repository logout when data source logout returns success`() {
        // Given
        val sourceResult = Result.success(Unit)
        every { authenticationDataSource.logout() } returns sourceResult

        // When
        val result = authenticationRepository.logout()

        // Then
        assertThat(result).isEqualTo(sourceResult)
    }

    @Test
    fun `should call data source check if first register when repository check if first register is called`() {
        // Given
        val sourceResult = Result.success(Unit)
        every { authenticationDataSource.checkIfFirstRegister() } returns sourceResult

        // When
        authenticationRepository.checkIfFirstRegister()

        // Then
        verify { authenticationDataSource.checkIfFirstRegister() }
    }

    @Test
    fun `should return failure from repository check if first register when data source check if first register returns failure`() {
        // Given
        val sourceResult = Result.failure<Unit>(Exception())
        every { authenticationDataSource.checkIfFirstRegister() } returns sourceResult

        // When
        val result = authenticationRepository.checkIfFirstRegister()

        // Then
        assertThat(result).isEqualTo(sourceResult)
    }

    @Test
    fun `should return success from repository check if first register when data source check if first register returns success`() {
        // Given
        val sourceResult = Result.success(Unit)
        every { authenticationDataSource.checkIfFirstRegister() } returns sourceResult

        // When
        val result = authenticationRepository.checkIfFirstRegister()

        // Then
        assertThat(result).isEqualTo(sourceResult)
    }

    @Test
    fun `should call data source checkEmail when repository checkEmail is called`() {
        // Given
        val email = "example@example.com"
        every { authenticationDataSource.checkEmail(email) } returns Result.success(Unit)

        // When
        val result = authenticationRepository.checkEmail(email)

        // Then
        verify(exactly = 1) { authenticationDataSource.checkEmail(email) }
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `should return failure when data source checkEmail returns failure`() {
        // Given
        val email = "example@example.com"
        val expectedError = Exception("Email not found")
        every { authenticationDataSource.checkEmail(email) } returns Result.failure(expectedError)

        // When
        val result = authenticationRepository.checkEmail(email)

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(expectedError)
    }

    @Test
    fun `should call data source getCurrentLoggedInUser when repository getCurrentLoggedInUser is called`() {
        // Given
        val expectedUser: User = createUserHelper(name = "John Doe", email = "john.doe@example.com")
        every { authenticationDataSource.getCurrentLoggedInUser() } returns Result.success(expectedUser)

        // When
        val result = authenticationRepository.getCurrentLoggedInUser()

        // Then
        verify(exactly = 1) { authenticationDataSource.getCurrentLoggedInUser() }
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(expectedUser)
    }

    @Test
    fun `should return null when data source getCurrentLoggedInUser returns null`() {
        // Given
        every { authenticationDataSource.getCurrentLoggedInUser() } returns Result.success(null)

        // When
        val result = authenticationRepository.getCurrentLoggedInUser()

        // Then
        verify(exactly = 1) { authenticationDataSource.getCurrentLoggedInUser() }
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isNull()
    }

    @Test
    fun `should return failure when data source getCurrentLoggedInUser returns failure`() {
        // Given
        val expectedError = Exception("No user currently logged in")
        every { authenticationDataSource.getCurrentLoggedInUser() } returns Result.failure(expectedError)

        // When
        val result = authenticationRepository.getCurrentLoggedInUser()

        // Then
        verify(exactly = 1) { authenticationDataSource.getCurrentLoggedInUser() }
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(expectedError)
    }

}