package ui.authentication_screens

import creator_helper.createUserHelper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.example.logic.use_cases.authentication.LoginUseCase
import data.csv.model.Role
import org.example.ui.common.components.Reader
import org.example.ui.authentication_screens.LoginScreen
import org.example.ui.admin.home_screen.AdminHomeScreen
import org.example.ui.mate.home_screen.MateHomeUI
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.example.ui.common.components.Printer

class LoginUseCaseUiScreenTest {
    private val reader: Reader = mockk(relaxed = true)
    private val printer: Printer = mockk(relaxed = true)
    private val loginUseCase: LoginUseCase = mockk()
    private val adminHomeScreen: AdminHomeScreen = mockk(relaxed = true)
    private val mateHomeUI: MateHomeUI = mockk(relaxed = true)
    private lateinit var loginScreen: LoginScreen

    @BeforeEach
    fun setUp() {
        loginScreen = LoginScreen(reader, printer, loginUseCase, adminHomeScreen, mateHomeUI)
    }

    @Test
    fun `should show title`() {
        // Given
        every { loginUseCase.login(any(), any()) } returns Result.success(createUserHelper())

        // When
        loginScreen.show()

        // Then
        verify(exactly = 1) { printer.printTitle("Login for Plan Mate") }
    }

    @Test
    fun `should prompt user for email`() {
        // Given
        every { loginUseCase.login(any(), any()) } returns Result.success(createUserHelper())
        every { reader.readInput() } returns "user@example.com"

        // When
        loginScreen.show()

        // Then
        verify { printer.printInfoLine("Email: ") }
    }

    @Test
    fun `should prompt user for password`() {
        // Given
        every { loginUseCase.login(any(), any()) } returns Result.success(createUserHelper())
        every { reader.readInput() } returns "password123"

        // When
        loginScreen.show()

        // Then
        verify { printer.printInfoLine("Password: ") }
    }

    @Test
    fun `should print error on invalid email input`() {
        // Given
        every { loginUseCase.login(any(), any()) } returns Result.success(createUserHelper())
        every { reader.readInput() } returnsMany listOf(null, "user@example.com")

        // When
        loginScreen.show()

        // Then
        verify { printer.printError("Invalid input") }
    }

    @Test
    fun `should print error on invalid password input`() {
        // Given
        every { loginUseCase.login(any(), any()) } returns Result.success(createUserHelper())
        every { reader.readInput() } returnsMany listOf("user@example.com", null, "password123")

        // When
        loginScreen.show()

        // Then
        verify { printer.printError("Invalid input") }
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
            printer.printInfoLine("Email: ")
            printer.printError("Invalid input")
            printer.printInfoLine("Email: ")
            printer.printInfoLine("Password: ")
            printer.printError("Invalid input")
            printer.printInfoLine("Password: ")
        }
    }

    @Test
    fun `should handle successful login and navigate to admin home screen if user is admin`() {
        // Given
        val testUser = createUserHelper(role = Role.ADMIN)
        every { reader.readInput() } returnsMany listOf("user@example.com", "password123")
        every { loginUseCase.login("user@example.com", "password123") } returns Result.success(testUser)

        // When
        loginScreen.show()

        // Then
        verify(exactly = 1) { printer.printInfoLine("Login successful!") }
        verify(exactly = 1) { adminHomeScreen.show() }
    }

    @Test
    fun `should handle successful login and navigate to mate home screen if user is mate`() {
        // Given
        val testUser = createUserHelper(role = Role.MATE)
        every { reader.readInput() } returnsMany listOf("user@example.com", "password123")
        every { loginUseCase.login("user@example.com", "password123") } returns Result.success(testUser)

        // When
        loginScreen.show()

        // Then
        verify(exactly = 1) { printer.printInfoLine("Login successful!") }
        verify(exactly = 1) { mateHomeUI.show() }
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
            printer.printInfoLine("Email: ")
            printer.printInfoLine("Password: ")
            printer.printError("Login failed!")
            printer.printInfoLine("Email: ")
            printer.printInfoLine("Password: ")
            printer.printInfoLine("Login successful!")
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
        verify { printer.printInfoLine("Email: ") }
        verify { printer.printInfoLine("Password: ") }
        verify { printer.printError("Login failed!") }
        verify { printer.printInfoLine("Email: ") }
        verify { printer.printInfoLine("Password: ") }
        verify { printer.printInfoLine("Login successful!") }
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
            printer.printInfoLine("Email: ")
            printer.printError("Invalid input")
            printer.printInfoLine("Email: ")
            printer.printInfoLine("Password: ")
            printer.printError("Invalid input")
            printer.printInfoLine("Password: ")
            printer.printInfoLine("Login successful!")
        }
    }
}