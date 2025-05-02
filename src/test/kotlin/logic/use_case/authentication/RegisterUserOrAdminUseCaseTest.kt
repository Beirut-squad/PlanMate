package logic.use_case.authentication

import com.google.common.truth.Truth.assertThat
import creator_helper.createUserHelper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.logic.repositories.authentication_repository.AuthenticationRepository
import org.example.logic.use_case.authentication.RegisterMateUseCase
import org.example.logic.use_case.authentication.RegisterUserOrAdminUseCase
import org.example.logic.use_case.authentication.encryption.EncryptPassword
import org.example.models.User
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RegisterUserOrAdminUseCaseTest {
    private val authenticationRepository: AuthenticationRepository = mockk(relaxed = true)
    private val encryptPassword: EncryptPassword = mockk(relaxed = true)
    private val registerMateUseCase: RegisterMateUseCase = mockk(relaxed = true)
    private lateinit var registerUserOrAdminUseCase: RegisterUserOrAdminUseCase

    @BeforeEach
    fun setup() {
        registerUserOrAdminUseCase =
            RegisterUserOrAdminUseCase(authenticationRepository, encryptPassword, registerMateUseCase)
    }

    @Test
    fun `should try to register normal user when trying to add an admin that is not the first ever user`() {
        // Given
        val user = createUserHelper()
        val encryptionResult: Result<String> = Result.success(user.password)
        every { encryptPassword.encryptPassword(user.password) } returns encryptionResult
        val repositoryResult: Result<Unit> = Result.failure(Exception())
        every { authenticationRepository.checkIfFirstRegister() } returns repositoryResult
        every { registerMateUseCase.addUser(any(), any(), any()) } returns Result.success(user)

        // When
        registerUserOrAdminUseCase.addAdmin(name = user.name, password = user.password, email = user.email)

        // Then
        verify { registerMateUseCase.addUser(name = user.name, password = user.password, email = user.email) }
    }

    @Test
    fun `should return failure when repository can't save admin`() {
        // Given
        val user = createUserHelper()
        val encryptionResult: Result<String> = Result.success(user.password)
        every { encryptPassword.encryptPassword(user.password) } returns encryptionResult
        val repositoryResult: Result<User> = Result.failure(Exception())
        every { authenticationRepository.checkIfFirstRegister() } returns Result.success(Unit)
        every { authenticationRepository.registerAdmin(any(), any(), any()) } returns repositoryResult

        // When
        val result = registerUserOrAdminUseCase.addAdmin(name = user.name, password = user.password, email = user.email)

        // Then
        assertThat(result).isEqualTo(repositoryResult)
    }

    @Test
    fun `should return success when repository can save`() {
        // Given
        val user = createUserHelper()
        val encryptionResult: Result<String> = Result.success(user.password)
        every { encryptPassword.encryptPassword(user.password) } returns encryptionResult
        val repositoryResult: Result<User> = Result.success(user)
        every { authenticationRepository.checkIfFirstRegister() } returns Result.success(Unit)
        every { authenticationRepository.registerAdmin(any(), any(), any()) } returns repositoryResult

        // When
        val result = registerUserOrAdminUseCase.addAdmin(name = user.name, password = user.password, email = user.email)

        // Then
        assertThat(result).isEqualTo(repositoryResult)
    }

    @Test
    fun `should encrypt password before saving it to repository`() {
        // Given
        val user = createUserHelper()
        val encryptionResult: Result<String> = Result.success("encrypted")
        every { authenticationRepository.checkIfFirstRegister() } returns Result.success(Unit)
        every { encryptPassword.encryptPassword(user.password) } returns encryptionResult
        every { authenticationRepository.registerAdmin(any(), any(), any()) } returns Result.success(user)

        // When
        registerUserOrAdminUseCase.addAdmin(name = user.name, password = user.password, email = user.email)

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
        every { encryptPassword.encryptPassword(user.password) } returns encryptionResult

        // When
        val result = registerUserOrAdminUseCase.addAdmin(name = user.name, password = user.password, email = user.email)

        // Then
        assertThat(result).isEqualTo(encryptionResult)
    }
}