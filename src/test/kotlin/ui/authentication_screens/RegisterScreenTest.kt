package ui.authentication_screens

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.ui.Reader
import org.example.ui.authentication_screens.RegisterScreen
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ui.Viewer

class RegisterScreenTest {
    private val reader: Reader = mockk(relaxed = true)
    private val viewer: Viewer = mockk(relaxed = true)
    private lateinit var registerScreen: RegisterScreen

    @BeforeEach
    fun setUp() {
        registerScreen = RegisterScreen(reader, viewer)
    }

    @Test
    fun `should show title`() {
        // When
        registerScreen.show()

        // Then
        verify(exactly = 1) { viewer.printTitle("Register for Plan Mate") }
    }

    @Test
       fun `should prompt user for username`() {
        // Given
        every { reader.readInput() } returns "testuser"

        // When
        registerScreen.show()

        // Then
        verify { viewer.printInfoLine("Name: ") }
    }

    @Test
    fun `should prompt user for password`() {
        // Given
        every { reader.readInput() } returns "password123"

        // When
        registerScreen.show()

        // Then
        verify { viewer.printInfoLine("Password: ") }
    }

    @Test
    fun `should print error on invalid username input`() {
        // Given
        every { reader.readInput() } returnsMany listOf(null, "testuser")

        // When
        registerScreen.show()

        // Then
        verify { viewer.printError("Invalid input") }
    }

    @Test
    fun `should print error on invalid password input`() {
        // Given
        every { reader.readInput() } returnsMany listOf(null, "password123")

        // When
        registerScreen.show()

        // Then
        verify { viewer.printError("Invalid input") }
    }
}