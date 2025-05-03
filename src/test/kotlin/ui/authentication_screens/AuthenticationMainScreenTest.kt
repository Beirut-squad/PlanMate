package ui.authentication_screens

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.ui.Reader
import org.example.ui.authentication_screens.AuthenticationMainScreen
import org.example.ui.authentication_screens.LoginScreen
import org.example.ui.authentication_screens.RegisterScreen
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ui.Viewer

class AuthenticationMainScreenTest {
    private val reader: Reader = mockk(relaxed = true)
    private val viewer: Viewer = mockk(relaxed = true)
    private val registerScreen: RegisterScreen = mockk(relaxed = true)
    private val loginScreen: LoginScreen = mockk(relaxed = true)
    private lateinit var authenticationMainScreen: AuthenticationMainScreen

    @BeforeEach
    fun setUp() {
        authenticationMainScreen = AuthenticationMainScreen(reader, viewer, registerScreen, loginScreen)
    }

    @Test
    fun `should show main screen title`() {
        // Given
        every { reader.readInt() } returns 1

        // When
        authenticationMainScreen.show()

        // Then
        verify(exactly = 1) { viewer.printTitle("Welcome to Plan Mate, what would you like to do?") }
    }

    @Test
    fun `should show Register and Login options`() {
        // Given
        every { reader.readInt() } returns 1

        // When
        authenticationMainScreen.show()

        // Then
        verify {
            viewer.printOptions(
                "Register",
                "Login"
            )
        }
    }

    @Test
    fun `should navigate to RegisterScreen when user selects Register`() {
        // Given
        every { reader.readInt() } returns 1

        // When
        authenticationMainScreen.show()

        // Then
        verify(exactly = 1) { registerScreen.show() }
        verify(exactly = 0) { loginScreen.show() }
    }

    @Test
    fun `should navigate to LoginScreen when user selects Login`() {
        // Given
        every { reader.readInt() } returns 2

        // When
        authenticationMainScreen.show()

        // Then
        verify(exactly = 1) { loginScreen.show() }
        verify(exactly = 0) { registerScreen.show() }
    }

    @Test
    fun `should print an error when an invalid option is selected`() {
        // Given
        every { reader.readInt() } returnsMany listOf(3, 1)

        // When
        authenticationMainScreen.show()

        // Then
        verify { viewer.printError("Invalid option") }
    }

    @Test
    fun `should stay in loop when an invalid option is selected`() {
        // Given
        every { reader.readInt() } returnsMany listOf(3, 1)

        // When
        authenticationMainScreen.show()

        // Then
        verify { viewer.printError("Invalid option") }
        verify(exactly = 2) {
            viewer.printOptions(
                "Register",
                "Login"
            )
        }
    }

    @Test
    fun `should break from loop when a valid option 1 is selected`() {
        // Given
        every { reader.readInt() } returns 1

        // When
        authenticationMainScreen.show()

        // Then
        verify { registerScreen.show() }
        verify(exactly = 1) {
            viewer.printOptions(
                "Register",
                "Login"
            )
        }
    }

    @Test
    fun `should break from loop when a valid option 2 is selected`() {
        // Given
        every { reader.readInt() } returns 2

        // When
        authenticationMainScreen.show()

        // Then
        verify { loginScreen.show() }
        verify(exactly = 1) {
            viewer.printOptions(
                "Register",
                "Login"
            )
        }
    }
}