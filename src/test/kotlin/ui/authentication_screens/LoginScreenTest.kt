package ui.authentication_screens

import creator_helper.createUserHelper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.example.logic.use_cases.authentication.LoginUseCase
import org.example.models.User
import org.example.ui.Reader
import org.example.ui.authentication_screens.LoginScreen
import org.example.ui.home_screens.HomeScreen
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ui.Viewer

class LoginScreenTest {
    private val reader: Reader = mockk(relaxed = true)
    private val viewer: Viewer = mockk(relaxed = true)
    private val loginUseCase: LoginUseCase = mockk()
    private val homeScreen: HomeScreen = mockk(relaxed = true)
    private lateinit var loginScreen: LoginScreen

    @BeforeEach
    fun setUp() {
        loginScreen = LoginScreen(reader, viewer, loginUseCase, homeScreen)
    }

    @Test
    fun `should show title`() {
        // Given
        every { loginUseCase.login(any(), any()) } returns Result.success(createUserHelper())

        // When
        loginScreen.show()

        // Then
        verify(exactly = 1) { viewer.printTitle("Login for Plan Mate") }
    }

    @Test
    fun `should prompt user for email`() {
        // Given
        every { loginUseCase.login(any(), any()) } returns Result.success(createUserHelper())
        every { reader.readInput() } returns "user@example.com"

        // When
        loginScreen.show()

        // Then
        verify { viewer.printInfoLine("Email: ") }
    }

    @Test
    fun `should prompt user for password`() {
        // Given
        every { loginUseCase.login(any(), any()) } returns Result.success(createUserHelper())
        every { reader.readInput() } returns "password123"

        // When
        loginScreen.show()

        // Then
        verify { viewer.printInfoLine("Password: ") }
    }

    @Test
    fun `should print error on invalid email input`() {
        // Given
        every { loginUseCase.login(any(), any()) } returns Result.success(createUserHelper())
        every { reader.readInput() } returnsMany listOf(null, "user@example.com")

        // When
        loginScreen.show()

        // Then
        verify { viewer.printError("Invalid input") }
    }

    @Test
    fun `should print error on invalid password input`() {
        // Given
        every { loginUseCase.login(any(), any()) } returns Result.success(createUserHelper())
        every { reader.readInput() } returnsMany listOf("user@example.com", null, "password123")

        // When
        loginScreen.show()

        // Then
        verify { viewer.printError("Invalid input") }
    }

    @Test
    fun `should loop until valid email and password are entered`() {
        // Given
        every { loginUseCase.login(any(), any()) } returns Result.success(createUserHelper())
        every { reader.readInput() } returnsMany listOf(null, "user@example.com", null, "password123")

        // When
        loginScreen.show()

        // Then
        verifyOrder {
            viewer.printInfoLine("Email: ")
            viewer.printError("Invalid input")
            viewer.printInfoLine("Email: ")
            viewer.printInfoLine("Password: ")
            viewer.printError("Invalid input")
            viewer.printInfoLine("Password: ")
        }
    }

    @Test
    fun `should handle successful login and navigate to home screen`() {
        // Given
        val testUser = mockk<User>(relaxed = true)
        every { reader.readInput() } returnsMany listOf("user@example.com", "password123")
        every { loginUseCase.login("user@example.com", "password123") } returns Result.success(testUser)

        // When
        loginScreen.show()

        // Then
        verify(exactly = 1) { viewer.printInfoLine("Login successful!") }
        verify(exactly = 1) { homeScreen.show() }
    }

    @Test
    fun `should handle failed login and retry login`() {
        // Given
        every { reader.readInput() } returnsMany listOf(
            "user@example.com",
            "wrongpassword",
            "user@example.com",
            "correctpassword"
        )
        every {
            loginUseCase.login(
                "user@example.com",
                "wrongpassword"
            )
        } returns Result.failure(Exception("Invalid credentials"))
        every {
            loginUseCase.login(
                "user@example.com",
                "correctpassword"
            )
        } returns Result.success(mockk(relaxed = true))

        // When
        loginScreen.show()

        // Then
        verifyOrder {
            viewer.printInfoLine("Email: ")
            viewer.printInfoLine("Password: ")
            viewer.printError("Login failed!")
            viewer.printInfoLine("Email: ")
            viewer.printInfoLine("Password: ")
            viewer.printInfoLine("Login successful!")
        }
    }

    @Test
    fun `should not proceed to home screen if login fails`() {
        // Given
        every { reader.readInput() } returnsMany listOf("user@example.com", "wrongpassword", "user@example.com", "correctpassword")
        every {
            loginUseCase.login(
                "user@example.com",
                "wrongpassword"
            )
        } returns Result.failure(Exception())
        every {
            loginUseCase.login(
                "user@example.com",
                "correctpassword"
            )
        } returns Result.success(createUserHelper())

        // When
        loginScreen.show()

        // Then
        verify(exactly = 1) { homeScreen.show() }
        verify { viewer.printError("Login failed!") }
    }

    @Test
    fun `should handle retry logic for all invalid inputs`() {
        // Given
        every { reader.readInput() } returnsMany listOf(
            null,
            "user@example.com",
            null,
            "password123"
        )
        every { loginUseCase.login("user@example.com", "password123") } returns Result.success(mockk(relaxed = true))

        // When
        loginScreen.show()

        // Then
        verifyOrder {
            viewer.printInfoLine("Email: ")
            viewer.printError("Invalid input")
            viewer.printInfoLine("Email: ")
            viewer.printInfoLine("Password: ")
            viewer.printError("Invalid input")
            viewer.printInfoLine("Password: ")
            viewer.printInfoLine("Login successful!")
        }
    }
}