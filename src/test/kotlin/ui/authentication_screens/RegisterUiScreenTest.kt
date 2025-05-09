package ui.authentication_screens

import creator_helper.createUserHelper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.example.logic.use_cases.authentication.RegisterUserOrAdminUseCase
import data.csv.model.User
import org.example.ui.common.components.Reader
import org.example.ui.authentication_screens.LoginScreen
import org.example.ui.authentication_screens.RegisterScreen
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.example.ui.common.components.Printer

class RegisterUiScreenTest {
    private val reader: Reader = mockk(relaxed = true)
    private val printer: Printer = mockk(relaxed = true)
    private val registerUseCase: RegisterUserOrAdminUseCase = mockk()
    private val loginScreen: LoginScreen = mockk(relaxed = true)
    private lateinit var registerScreen: RegisterScreen

    @BeforeEach
    fun setUp() {
        registerScreen = RegisterScreen(reader, printer, registerUseCase, loginScreen)
    }

    @Test
    fun `should show title`() {
        // Given
        every { registerUseCase.add(any(), any(), any()) } returns Result.success(createUserHelper())

        // When
        registerScreen.show()

        // Then
        verify(exactly = 1) { printer.printTitle("Register for Plan Mate") }
    }

    @Test
    fun `should prompt user for name`() {
        // Given
        every { registerUseCase.add(any(), any(), any()) } returns Result.success(createUserHelper())
        every { reader.readInput() } returns "John Doe"

        // When
        registerScreen.show()

        // Then
        verify { printer.printInfoLine("Name: ") }
    }

    @Test
    fun `should prompt user for email`() {
        // Given
        every { registerUseCase.add(any(), any(), any()) } returns Result.success(createUserHelper())
        every { reader.readInput() } returns "user@example.com"

        // When
        registerScreen.show()

        // Then
        verify { printer.printInfoLine("Email: ") }
    }

    @Test
    fun `should prompt user for password`() {
        // Given
        every { registerUseCase.add(any(), any(), any()) } returns Result.success(createUserHelper())
        every { reader.readInput() } returns "password123"

        // When
        registerScreen.show()

        // Then
        verify { printer.printInfoLine("Password: ") }
    }

    @Test
    fun `should print error on invalid name input`() {
        // Given
        every { registerUseCase.add(any(), any(), any()) } returns Result.success(createUserHelper())
        every { reader.readInput() } returnsMany listOf(null, "John Doe")

        // When
        registerScreen.show()

        // Then
        verify { printer.printError("Invalid input") }
    }

    @Test
    fun `should print error on invalid email input`() {
        // Given
        every { registerUseCase.add(any(), any(), any()) } returns Result.success(createUserHelper())
        every { reader.readInput() } returnsMany listOf("John Doe", null, "user@example.com")

        // When
        registerScreen.show()

        // Then
        verify { printer.printError("Invalid input") }
    }

    @Test
    fun `should print error on invalid password input`() {
        // Given
        every { registerUseCase.add(any(), any(), any()) } returns Result.success(createUserHelper())
        every { reader.readInput() } returnsMany listOf("John Doe", "user@example.com", null, "password123")

        // When
        registerScreen.show()

        // Then
        verify { printer.printError("Invalid input") }
    }

    @Test
    fun `should loop until valid name, email, and password are entered`() {
        // Given
        every { registerUseCase.add(any(), any(), any()) } returns Result.success(createUserHelper())
        every { reader.readInput() } returnsMany listOf(null, "John Doe", null, "user@example.com", null, "password123")

        // When
        registerScreen.show()

        // Then
        verifyOrder {
            printer.printInfoLine("Name: ")
            printer.printError("Invalid input")
            printer.printInfoLine("Name: ")
            printer.printInfoLine("Email: ")
            printer.printError("Invalid input")
            printer.printInfoLine("Email: ")
            printer.printInfoLine("Password: ")
            printer.printError("Invalid input")
            printer.printInfoLine("Password: ")
        }
    }

    @Test
    fun `should handle successful registration and navigate to home screen`() {
        // Given
        val testUser = mockk<User>(relaxed = true)
        every { reader.readInput() } returnsMany listOf("John Doe", "user@example.com", "password123")
        every { registerUseCase.add("John Doe", "password123", "user@example.com") } returns Result.success(testUser)

        // When
        registerScreen.show()

        // Then
        verify(exactly = 1) { printer.printInfoLine("Register successfully!") }
        verify(exactly = 1) { loginScreen.show() }
    }

    @Test
    fun `should handle failed registration and retry`() {
        // Given
        every { reader.readInput() } returnsMany listOf(
            "John Doe", "user@example.com", "weakpassword",
            "John Doe", "user@example.com", "strongpassword"
        )
        every {
            registerUseCase.add("John Doe", "weakpassword", "user@example.com")
        } returns Result.failure(Exception("Registration failed"))

        every {
            registerUseCase.add("John Doe", "strongpassword", "user@example.com")
        } returns Result.success(mockk(relaxed = true))


        // When
        registerScreen.show()

        // Then
        verifyOrder {
            printer.printInfoLine("Name: ")
            printer.printInfoLine("Email: ")
            printer.printInfoLine("Password: ")
            printer.printError("Register failed!")
            printer.printInfoLine("Name: ")
            printer.printInfoLine("Email: ")
            printer.printInfoLine("Password: ")
            printer.printInfoLine("Register successfully!")
        }
        verify { loginScreen.show() }
    }

    @Test
    fun `should not proceed to home screen if registration fails`() {
        // Given

        every { reader.readInput() } returnsMany listOf(
            "John Doe",
            "user1@example.com",
            "wrongpassword",
            "John Doe",
            "user2@example.com",
            "goodpassword"
        )
        every {
            registerUseCase.add(name = "John Doe", email = "user1@example.com", password = "wrongpassword")
        } returns Result.failure(Exception("Registration failed"))
        every {
            registerUseCase.add(name = "John Doe", email = "user2@example.com", password = "goodpassword")
        } returns Result.success(createUserHelper())

        // When
        registerScreen.show()

        // Then
        verify(exactly = 1) { loginScreen.show() }
        verify { printer.printError("Register failed!") }
    }

    @Test
    fun `should handle retry logic for all invalid inputs`() {
        // Given
        every { reader.readInput() } returnsMany listOf(
            null,
            "John Doe",
            null,
            "user@example.com",
            null,
            "password123"
        )
        every {
            registerUseCase.add(
                name = "John Doe",
                email = "user@example.com",
                password = "password123"
            )
        } returns Result.success(mockk(relaxed = true))

        // When
        registerScreen.show()

        // Then
        verifyOrder {
            printer.printInfoLine("Name: ")
            printer.printError("Invalid input")
            printer.printInfoLine("Name: ")
            printer.printInfoLine("Email: ")
            printer.printError("Invalid input")
            printer.printInfoLine("Email: ")
            printer.printInfoLine("Password: ")
            printer.printError("Invalid input")
            printer.printInfoLine("Password: ")
            printer.printInfoLine("Register successfully!")
        }
    }
}