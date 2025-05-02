package data.datasource.authentication_data_source

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.data.csv.CsvReader
import org.example.data.csv.CsvWriter
import org.example.data.datasource.authentication_data_source.AuthenticationDataSourceImpl
import org.example.logic.exceptions.authentication_exceptions.*
import org.example.models.Role
import org.example.models.User
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class AuthenticationDataSourceImplTest {

    private lateinit var authenticationDataSource: AuthenticationDataSourceImpl
    private val csvWriter: CsvWriter<User> = mockk(relaxed = true)
    private val csvReader: CsvReader<User> = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        authenticationDataSource = AuthenticationDataSourceImpl(csvWriter, csvReader)
    }

    // --- Testing login ---
    @Test
    fun `login should return success when email and password match`() {
        // Given
        val user = User(UUID.randomUUID(), "Test User", "password123", "test@example.com", Role.MATE, false)
        every { csvReader.read(any()) } returns listOf(user)

        // When
        val result = authenticationDataSource.login(email = user.email, password = user.password)

        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(user)
    }

    @Test
    fun `login should return failure when email does not match`() {
        // Given
        every { csvReader.read(any()) } returns listOf(
            User(UUID.randomUUID(), "Test User", "password123", "test@example.com", Role.MATE, false)
        )

        // When
        val result = authenticationDataSource.login(email = "wrong@example.com", password = "password123")

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(InvalidEmailOrPasswordException::class.java)
    }

    @Test
    fun `login should return failure when password does not match`() {
        // Given
        every { csvReader.read(any()) } returns listOf(
            User(UUID.randomUUID(), "Test User", "password123", "test@example.com", Role.MATE, false)
        )

        // When
        val result = authenticationDataSource.login(email = "test@example.com", password = "wrongpassword")

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(InvalidEmailOrPasswordException::class.java)
    }

    @Test
    fun `login should return failure if saving current user fails`() {
        // Given
        val user = User(UUID.randomUUID(), "Test User", "password123", "test@example.com", Role.MATE, false)

        every { csvReader.read(any()) } returns listOf(user)
        every { csvWriter.writeToFile(any(), "current_user.csv") } throws Exception("IO Error")

        // When
        val result = authenticationDataSource.login(email = user.email, password = user.password)

        // Then
        assertThat(result.isFailure).isTrue()
    }

    // --- Testing checkEmail ---
    @Test
    fun `checkEmail should return success if email exists`() {
        // Given
        val user = User(UUID.randomUUID(), "Test User", "password123", "test@example.com", Role.MATE, false)
        every { csvReader.read(any()) } returns listOf(user)

        // When
        val result = authenticationDataSource.checkEmail(email = user.email)

        // Then
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `checkEmail should return failure if email does not exist`() {
        // Given
        every { csvReader.read(any()) } returns emptyList()

        // When
        val result = authenticationDataSource.checkEmail(email = "nonexistent@example.com")

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(EmailNotFoundException::class.java)
    }

    // --- Testing register ---
    @Test
    fun `register should return success when user is not already registered`() {
        // Given
        val name = "New User"
        val email = "new@example.com"
        val password = "password123"

        every { csvReader.read(any()) } returns emptyList()

        // When
        val result = authenticationDataSource.register(name, password, email)

        // Then
        assertThat(result.isSuccess).isTrue()
        verify { csvWriter.writeToFile(any(), "registered_users.csv") }
        verify { csvWriter.writeToFile(any(), "current_user.csv") }
    }

    @Test
    fun `register should return failure when email is already registered`() {
        // Given
        val user = User(UUID.randomUUID(), "Test User", "password123", "test@example.com", Role.MATE, false)

        every { csvReader.read(any()) } returns listOf(user)

        // When
        val result = authenticationDataSource.register(name = "Another User", password = "newpass", email = user.email)

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(EmailAlreadyExistsException::class.java)
    }

    @Test
    fun `register should return failure when saving to current user fails`() {
        // Given
        val name = "New User"
        val email = "new@example.com"
        val password = "password123"

        every { csvReader.read(any()) } returns emptyList()
        every { csvWriter.writeToFile(any(), "current_user.csv") } throws Exception()

        // When
        val result = authenticationDataSource.register(name, password, email)

        // Then
        assertThat(result.isFailure).isTrue()
    }

    // --- Testing registerAdmin ---
    @Test
    fun `registerAdmin should return success when admin is not already registered`() {
        // Given
        val name = "Admin User"
        val email = "admin@example.com"
        val password = "adminpass123"

        every { csvReader.read(any()) } returns emptyList()

        // When
        val result = authenticationDataSource.registerAdmin(name, password, email)

        // Then
        assertThat(result.isSuccess).isTrue()
        val user = result.getOrNull()
        assertThat(user?.role).isEqualTo(Role.ADMIN)
        verify { csvWriter.writeToFile(any(), "registered_users.csv") }
        verify { csvWriter.writeToFile(any(), "current_user.csv") }
    }

    @Test
    fun `registerAdmin should return failure when admin email is already registered`() {
        // Given
        val user = User(UUID.randomUUID(), "Admin User", "adminpass123", "admin@example.com", Role.ADMIN, false)

        every { csvReader.read(any()) } returns listOf(user)

        // When
        val result = authenticationDataSource.registerAdmin("New Admin", "newpass123", user.email)

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(EmailAlreadyExistsException::class.java)
    }

    @Test
    fun `registerAdmin should return failure when saving to current user fails`() {
        // Given
        val name = "New Admin"
        val email = "admin@example.com"
        val password = "password123"

        every { csvReader.read(any()) } returns emptyList()
        every { csvWriter.writeToFile(any(), "current_user.csv") } throws Exception()

        // When
        val result = authenticationDataSource.registerAdmin(name, password, email)

        // Then
        assertThat(result.isFailure).isTrue()
    }

    // --- Testing logout ---
    @Test
    fun `logout should return success when a user is logged in`() {
        // Given
        val user = User(UUID.randomUUID(), "Logged In User", "password123", "user@example.com", Role.MATE, false)
        every { csvReader.read(any()) } returns listOf(user)

        // When
        val result = authenticationDataSource.logout()

        // Then
        assertThat(result.isSuccess).isTrue()
        verify { csvWriter.writeToFile(emptyList<User>(), "current_user.csv") }
    }

    @Test
    fun `logout should return failure when user is null`() {
        // Given
        every { csvReader.read(any()) } returns emptyList()

        // When
        val result = authenticationDataSource.logout()

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(NoLoggedInUserException::class.java)
    }

    // --- Testing checkIfFirstRegister ---
    @Test
    fun `checkIfFirstRegister should return success if no users exist`() {
        // Given
        every { csvReader.read(any()) } returns emptyList()

        // When
        val result = authenticationDataSource.checkIfFirstRegister()

        // Then
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `checkIfFirstRegister should return failure if users already exist`() {
        // Given
        val user = User(UUID.randomUUID(), "Existing User", "password123", "existing@example.com", Role.MATE, false)

        every { csvReader.read(any()) } returns listOf(user)

        // When
        val result = authenticationDataSource.checkIfFirstRegister()

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(UsersAlreadyExistException::class.java)
    }
}