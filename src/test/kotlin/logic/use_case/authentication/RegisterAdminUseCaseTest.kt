package logic.use_case.authentication

import com.google.common.truth.Truth.assertThat
import creator_helper.createUserHelper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.logic.repositories.authentication_repository.AuthenticationRepository
import org.example.logic.use_case.authentication.RegisterAdminUseCase
import org.example.logic.use_case.authentication.encryption.EncryptPasswordUseCase
import org.example.models.User
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RegisterAdminUseCaseTest {
    private val authenticationRepository: AuthenticationRepository = mockk(relaxed = true)
    private val encryptPasswordUseCase: EncryptPasswordUseCase = mockk(relaxed = true)
    private lateinit var registerAdminUseCase: RegisterAdminUseCase

    @BeforeEach
    fun setup() {
        registerAdminUseCase = RegisterAdminUseCase(authenticationRepository, encryptPasswordUseCase)
    }

    @Test
    fun `should return failure when trying to add an admin that is not the first ever user`() {
        // Given
        val user = createUserHelper()
        val encryptionResult: Result<String> = Result.success(user.password)
        every { encryptPasswordUseCase.encryptPassword(user.password) } returns encryptionResult
        val repositoryResult: Result<Unit> = Result.failure(Exception())
        every { authenticationRepository.checkIfFirstRegister() } returns repositoryResult

        // When
        val result = registerAdminUseCase.addAdmin(name = user.name, password = user.password, email = user.email)

        // Then
        assertThat(result).isEqualTo(repositoryResult)
    }

    @Test
    fun `should return failure when repository can't save admin`() {
        // Given
        val user = createUserHelper()
        val encryptionResult: Result<String> = Result.success(user.password)
        every { encryptPasswordUseCase.encryptPassword(user.password) } returns encryptionResult
        val repositoryResult: Result<User> = Result.failure(Exception())
        every { authenticationRepository.checkIfFirstRegister() } returns Result.success(Unit)
        every { authenticationRepository.registerAdmin(any(), any(), any()) } returns repositoryResult

        // When
        val result = registerAdminUseCase.addAdmin(name = user.name, password = user.password, email = user.email)

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
        every { authenticationRepository.checkIfFirstRegister() } returns Result.success(Unit)
        every { authenticationRepository.registerAdmin(any(), any(), any()) } returns repositoryResult

        // When
        val result = registerAdminUseCase.addAdmin(name = user.name, password = user.password, email = user.email)

        // Then
        assertThat(result).isEqualTo(repositoryResult)
    }

    @Test
    fun `should encrypt password before saving it to repository`() {
        // Given
        val user = createUserHelper()
        val encryptionResult: Result<String> = Result.success("encrypted")
        every { authenticationRepository.checkIfFirstRegister() } returns Result.success(Unit)
        every { encryptPasswordUseCase.encryptPassword(user.password) } returns encryptionResult
        every { authenticationRepository.registerAdmin(any(), any(), any()) } returns Result.success(user)

        // When
        registerAdminUseCase.addAdmin(name = user.name, password = user.password, email = user.email)

        // Then
        verify {
            authenticationRepository.registerAdmin(
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
        every { authenticationRepository.checkIfFirstRegister() } returns Result.success(Unit)
        every { encryptPasswordUseCase.encryptPassword(user.password) } returns encryptionResult

        // When
        val result = registerAdminUseCase.addAdmin(name = user.name, password = user.password, email = user.email)

        // Then
        assertThat(result).isEqualTo(encryptionResult)
    }
}