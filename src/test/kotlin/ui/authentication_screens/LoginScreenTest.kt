package ui.authentication_screens

import creator_helper.createUserHelper
import io.mockk.*
import org.example.constants.StringConstants
import org.example.logic.exceptions.ErrorHandler
import org.example.logic.use_cases.authentication.LoginUseCase
import org.example.models.Role
import org.example.ui.Reader
import org.example.ui.authentication_screens.LoginScreen
import org.example.ui.home_screens.admin.ui.home_screens.admin.AdminHomeScreen
import org.example.ui.home_screens.mate.ui.home_screens.mate.MateHomeScreen
import org.example.ui.utils.InputHandler
import org.junit.After
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ui.Viewer

class LoginScreenTest {
    private val inputHandler: InputHandler = mockk(relaxed = true)
    private val errorHandler: ErrorHandler = mockk(relaxed = true)
    private val reader: Reader = mockk(relaxed = true)
    private val viewer: Viewer = mockk(relaxed = true)
    private val loginUseCase: LoginUseCase = mockk(relaxed = true)
    private val adminHomeScreen: AdminHomeScreen = mockk(relaxed = true)
    private val mateHomeScreen: MateHomeScreen = mockk(relaxed = true)
    private lateinit var loginScreen: LoginScreen
    private val testEmail = "user@example.com"
    private val testPassword = "password123"
    private val adminUser = createUserHelper(role = Role.ADMIN)
    private val mateUser = createUserHelper(role = Role.MATE)

    @BeforeEach
    fun setUp() {
        loginScreen =
            LoginScreen(reader, viewer, loginUseCase, adminHomeScreen, mateHomeScreen, inputHandler, errorHandler)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }



    @Test
    fun `show should display title and prompt for login details`() {
        // Given
        every { inputHandler.takeInput(viewer, reader, StringConstants.AuthScreen.EMAIL) } returns testEmail
        every { inputHandler.takeInput(viewer, reader, StringConstants.AuthScreen.PASSWORD) } returns testPassword
        every { loginUseCase.login(testEmail, testPassword) } returns Result.success(adminUser)

        // When
        loginScreen.show()

        // Then
        verifyOrder {
            viewer.printTitle(StringConstants.LoginScreen.WELCOME_LOGIN)
            viewer.printInfoLine(StringConstants.LoginScreen.LOGIN_DETAILS)
            inputHandler.takeInput(viewer, reader, StringConstants.AuthScreen.EMAIL)
            inputHandler.takeInput(viewer, reader, StringConstants.AuthScreen.PASSWORD)
            loginUseCase.login(testEmail, testPassword)
            viewer.printInfoLine(StringConstants.LoginScreen.LOGIN_SUCCESS)
            adminHomeScreen.show()
        }
    }

    @Test
    fun `successful login as admin should navigate to admin home screen`() {
        // Arrange
        every { inputHandler.takeInput(viewer, reader, StringConstants.AuthScreen.EMAIL) } returns testEmail
        every { inputHandler.takeInput(viewer, reader, StringConstants.AuthScreen.PASSWORD) } returns testPassword
        every { loginUseCase.login(testEmail, testPassword) } returns Result.success(adminUser)

        // Act
        loginScreen.show()

        // Assert
        verify(exactly = 1) { adminHomeScreen.show() }
        verify(exactly = 0) { mateHomeScreen.show() }
    }

    @Test
    fun `successful login as mate should navigate to mate home screen`() {
        // Arrange
        every { inputHandler.takeInput(viewer, reader, StringConstants.AuthScreen.EMAIL) } returns testEmail
        every { inputHandler.takeInput(viewer, reader, StringConstants.AuthScreen.PASSWORD) } returns testPassword
        every { loginUseCase.login(testEmail, testPassword) } returns Result.success(mateUser)

        // Act
        loginScreen.show()

        // Assert
        verify(exactly = 0) { adminHomeScreen.show() }
        verify(exactly = 1) { mateHomeScreen.show() }
    }

    @Test
    fun `failed login should handle error and retry login`() {
        // Arrange
        val testException = Exception("Invalid credentials")
        val captor = slot<() -> Unit>()

        // Define behavior for first attempt (failure)
        every { inputHandler.takeInput(viewer, reader, StringConstants.AuthScreen.EMAIL) } returnsMany listOf(testEmail, "retry@example.com")
        every { inputHandler.takeInput(viewer, reader, StringConstants.AuthScreen.PASSWORD) } returnsMany listOf(testPassword, "retry123")
        every { loginUseCase.login(testEmail, testPassword) } returns Result.failure(testException)
        every { errorHandler.handle(testException) } just runs

        // Define behavior for second attempt (success)
        every { loginUseCase.login("retry@example.com", "retry123") } returns Result.success(adminUser)
        every { viewer.printInfoLine(StringConstants.LoginScreen.LOGIN_SUCCESS) } just runs
        every { adminHomeScreen.show() } just runs

        // Act
        loginScreen.show()

        // Assert
        verifySequence {
            viewer.printTitle(StringConstants.LoginScreen.WELCOME_LOGIN)
            viewer.printInfoLine(StringConstants.LoginScreen.LOGIN_DETAILS)
            inputHandler.takeInput(viewer, reader, StringConstants.AuthScreen.EMAIL)
            inputHandler.takeInput(viewer, reader, StringConstants.AuthScreen.PASSWORD)
            loginUseCase.login(testEmail, testPassword)
            errorHandler.handle(testException)
            inputHandler.takeInput(viewer, reader, StringConstants.AuthScreen.EMAIL)
            inputHandler.takeInput(viewer, reader, StringConstants.AuthScreen.PASSWORD)
            loginUseCase.login("retry@example.com", "retry123")
            viewer.printInfoLine(StringConstants.LoginScreen.LOGIN_SUCCESS)
            adminHomeScreen.show()
        }
    }


    fun `recursive login attempts should work until successful`() {
        // Arrange
        val testException = Exception("Invalid credentials")

        // First attempt - failure
        every { inputHandler.takeInput(viewer, reader, StringConstants.AuthScreen.EMAIL) } returnsMany
                listOf(testEmail, "second@example.com", "third@example.com")
        every { inputHandler.takeInput(viewer, reader, StringConstants.AuthScreen.PASSWORD) } returnsMany
                listOf(testPassword, "password2", "password3")
        every { loginUseCase.login(testEmail, testPassword) } returns Result.failure(testException)
        every { errorHandler.handle(testException) } just runs

        // Second attempt - failure
        every { loginUseCase.login("second@example.com", "password2") } returns Result.failure(testException)

        // Third attempt - success
        every { loginUseCase.login("third@example.com", "password3") } returns Result.success(mateUser)
        every { viewer.printInfoLine(StringConstants.LoginScreen.LOGIN_SUCCESS) } just runs
        every { mateHomeScreen.show() } just runs

        // Act
        loginScreen.show()

        // Assert
        verify(exactly = 2) { errorHandler.handle(testException) }
        verify(exactly = 1) { mateHomeScreen.show() }
    }
}