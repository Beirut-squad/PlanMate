package ui.authentication_screens

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.example.ui.Reader
import org.example.ui.authentication_screens.LoginScreen
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ui.Viewer

class LoginScreenTest {
    private val reader: Reader = mockk(relaxed = true)
    private val viewer: Viewer = mockk(relaxed = true)
    private lateinit var loginScreen: LoginScreen

    @BeforeEach
    fun setUp() {
        loginScreen = LoginScreen(reader, viewer)
    }

    @Test
    fun `should show title`() {
        // When
        loginScreen.show()

        // Then
        verify(exactly = 1) { viewer.printTitle("Login for Plan Mate") }
    }

    @Test
    fun `should prompt user for email`() {
        // Given
        every { reader.readInput() } returns "user@example.com"

        // When
        loginScreen.show()

        // Then
        verify { viewer.printInfoLine("Email: ") }
    }

    @Test
    fun `should prompt user for password`() {
        // Given
        every { reader.readInput() } returns "password123"

        // When
        loginScreen.show()

        // Then
        verify { viewer.printInfoLine("Password: ") }
    }

    @Test
    fun `should print error on invalid email input`() {
        // Given
        every { reader.readInput() } returnsMany listOf(null, "user@example.com")

        // When
        loginScreen.show()

        // Then
        verify { viewer.printError("Invalid input") }
    }

    @Test
    fun `should print error on invalid password input`() {
        // Given
        every { reader.readInput() } returnsMany listOf(null, "password123")

        // When
        loginScreen.show()

        // Then
        verify { viewer.printError("Invalid input") }
    }

    @Test
    fun `should loop until valid email and password are entered`() {
        // Given
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

}