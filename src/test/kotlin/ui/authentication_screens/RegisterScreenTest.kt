package ui.authentication_screens

import creator_helper.createUserHelper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.example.constants.StringConstants
import org.example.logic.exceptions.ErrorHandler
import org.example.logic.use_cases.authentication.LoginUseCase
import org.example.logic.use_cases.authentication.RegisterUserOrAdminUseCase
import org.example.models.Role
import org.example.models.User
import org.example.ui.Reader
import org.example.ui.authentication_screens.LoginScreen
import org.example.ui.authentication_screens.RegisterScreen
import org.example.ui.home_screens.admin.ui.home_screens.admin.AdminHomeScreen
import org.example.ui.home_screens.mate.ui.home_screens.mate.MateHomeScreen
import org.example.ui.utils.InputHandler
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ui.Viewer
import kotlin.math.log

class RegisterScreenTest {

    private val inputHandler: InputHandler = mockk(relaxed = true)
    private val errorHandler: ErrorHandler = mockk(relaxed = true)
    private val reader: Reader = mockk(relaxed = true)
    private val viewer: Viewer = mockk(relaxed = true)
    private val registerUseCase: RegisterUserOrAdminUseCase = mockk()
    private val loginScreen: LoginScreen = mockk(relaxed = true)
    private lateinit var registerScreen: RegisterScreen
    private val testName = "Test User"
    private val testEmail = "user@example.com"
    private val testPassword = "password123"
    private val adminUser = createUserHelper(role = Role.ADMIN)

    @BeforeEach
    fun setUp() {
        registerScreen = RegisterScreen(
            reader = reader,
            viewer = viewer,
            registerUseCase = registerUseCase,
            loginScreen = loginScreen,
            inputHandler = inputHandler,
            errorHandler = errorHandler
        )
    }

    @Test
    fun `show should display title and prompt for registration details`() {
        // Given
        every { inputHandler.takeInput(viewer, reader, StringConstants.RegisterScreen.NAME) } returns testName
        every { inputHandler.takeInput(viewer, reader, StringConstants.AuthScreen.EMAIL) } returns testEmail
        every { inputHandler.takeInput(viewer, reader, StringConstants.AuthScreen.PASSWORD) } returns testPassword
        every { registerUseCase.add(name = testName, email = testEmail, password = testPassword) } returns Result.success(adminUser)

        // When
        registerScreen.show()

        // Verify
        verifyOrder {
            viewer.printTitle(StringConstants.RegisterScreen.WELCOME_REGISTER)
            viewer.printInfoLine(StringConstants.RegisterScreen.REGISTRATION_DETAILS)
            inputHandler.takeInput(viewer, reader, StringConstants.RegisterScreen.NAME)
            inputHandler.takeInput(viewer, reader, StringConstants.AuthScreen.EMAIL)
            inputHandler.takeInput(viewer, reader, StringConstants.AuthScreen.PASSWORD)
            registerUseCase.add(name = testName, email = testEmail, password = testPassword)
            viewer.printInfoLine(StringConstants.RegisterScreen.REGISTER_SUCCESS)
            loginScreen.show()
        }
    }

    @Test
    fun `successful registration should navigate to login screen`() {
        // Given
        every { inputHandler.takeInput(viewer, reader, StringConstants.RegisterScreen.NAME) } returns testName
        every { inputHandler.takeInput(viewer, reader, StringConstants.AuthScreen.EMAIL) } returns testEmail
        every { inputHandler.takeInput(viewer, reader, StringConstants.AuthScreen.PASSWORD) } returns testPassword
        every { registerUseCase.add(name = testName, email = testEmail, password = testPassword) } returns Result.success(adminUser)

        // When
        registerScreen.show()

        // Then
        verify(exactly = 1) { loginScreen.show() }
    }

    @Test
    fun `failed registration should handle error and retry registration`() {
        // Given
        val testException = Exception("Registration failed")

        // First attempt - failure
        every { inputHandler.takeInput(viewer, reader, StringConstants.RegisterScreen.NAME) } returnsMany
                listOf(testName, "Retry User")
        every { inputHandler.takeInput(viewer, reader, StringConstants.AuthScreen.EMAIL) } returnsMany
                listOf(testEmail, "retry@example.com")
        every { inputHandler.takeInput(viewer, reader, StringConstants.AuthScreen.PASSWORD) } returnsMany
                listOf(testPassword, "retry123")
        every { registerUseCase.add(name = testName, email = testEmail, password = testPassword) } returns
                Result.failure(testException)

        // Second attempt - success
        every { registerUseCase.add(name = "Retry User", email = "retry@example.com", password = "retry123") } returns Result.success(adminUser)

        // When
        registerScreen.show()

        // Then
        verifyOrder {
            viewer.printTitle(StringConstants.RegisterScreen.WELCOME_REGISTER)
            viewer.printInfoLine(StringConstants.RegisterScreen.REGISTRATION_DETAILS)
            inputHandler.takeInput(viewer, reader, StringConstants.RegisterScreen.NAME)
            inputHandler.takeInput(viewer, reader, StringConstants.AuthScreen.EMAIL)
            inputHandler.takeInput(viewer, reader, StringConstants.AuthScreen.PASSWORD)
            registerUseCase.add(name = testName, email = testEmail, password = testPassword)
            errorHandler.handle(testException)
            inputHandler.takeInput(viewer, reader, StringConstants.RegisterScreen.NAME)
            inputHandler.takeInput(viewer, reader, StringConstants.AuthScreen.EMAIL)
            inputHandler.takeInput(viewer, reader, StringConstants.AuthScreen.PASSWORD)
            registerUseCase.add(name = "Retry User", email = "retry@example.com", password = "retry123")
            viewer.printInfoLine(StringConstants.RegisterScreen.REGISTER_SUCCESS)
            loginScreen.show()
        }
    }

    @Test
    fun `recursive registration attempts should work until successful`() {
        // Given
        val testException = Exception("Registration failed")

        // First attempt - failure
        every { inputHandler.takeInput(viewer, reader, StringConstants.RegisterScreen.NAME) } returnsMany
                listOf(testName, "Second User", "Third User")
        every { inputHandler.takeInput(viewer, reader, StringConstants.AuthScreen.EMAIL) } returnsMany
                listOf(testEmail, "second@example.com", "third@example.com")
        every { inputHandler.takeInput(viewer, reader, StringConstants.AuthScreen.PASSWORD) } returnsMany
                listOf(testPassword, "password2", "password3")
        every { registerUseCase.add(name = testName, email = testEmail, password = testPassword) } returns
                Result.failure(testException)

        // Second attempt - failure
        every { registerUseCase.add(name = "Second User", email = "second@example.com", password = "password2") } returns Result.failure(testException)

        // Third attempt - success
        every { registerUseCase.add(name = "Third User", email = "third@example.com", password = "password3") } returns Result.success(adminUser)

        // When
        registerScreen.show()

        // Then
        verify(exactly = 2) { errorHandler.handle(testException) }
        verify(exactly = 1) { loginScreen.show() }
        verify(exactly = 1) { viewer.printInfoLine(StringConstants.RegisterScreen.REGISTER_SUCCESS) }
    }

    @Test
    fun `show should display proper registration flow`() {
        // Given
        every { inputHandler.takeInput(viewer, reader, StringConstants.RegisterScreen.NAME) } returns testName
        every { inputHandler.takeInput(viewer, reader, StringConstants.AuthScreen.EMAIL) } returns testEmail
        every { inputHandler.takeInput(viewer, reader, StringConstants.AuthScreen.PASSWORD) } returns testPassword
        every { registerUseCase.add(name = testName, email = testEmail, password = testPassword) } returns Result.success(adminUser)

        // When
        registerScreen.show()

        // Then
        verify {
            viewer.printTitle(StringConstants.RegisterScreen.WELCOME_REGISTER)
            viewer.printInfoLine(StringConstants.RegisterScreen.REGISTRATION_DETAILS)
            viewer.printInfoLine(StringConstants.RegisterScreen.REGISTER_SUCCESS)
        }
    }
}