package ui.view.authentication

import ui.common.exception.EmailAlreadyExistsException
import ui.common.exception.EmptyFieldException
import ui.common.exception.handler.ExceptionHandler
import ui.common.exception.handler.SafeExecutor
import domain.useCase.authentication.RegisterUserUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import ui.common.Printer
import ui.common.Reader
import ui.common.Validator
import kotlin.test.Test

class RegisterUiTest {

    private val reader: Reader = mockk(relaxed = true)
    private val printer: Printer = mockk(relaxed = true)
    private val registerUseCase: RegisterUserUseCase = mockk(relaxed = true)
    private val loginUi: LoginUi = mockk(relaxed = true)
    private val executor: SafeExecutor = spyk()
    private val handler: ExceptionHandler = mockk(relaxed = true)
    private val validator: Validator = mockk(relaxed = true)

    private lateinit var registerUi: RegisterUi

    @BeforeEach
    fun setup() {
        registerUi = RegisterUi(
            reader,
            printer,
            registerUseCase,
            loginUi,
            executor,
            handler,
            validator
        )
    }

    @Test
    fun `should print title and prompt messages when show function is called`() = runTest {
        registerUi.show()

        coVerify { printer.printTitle("Register for Plan Mate") }
        coVerify { printer.printInfoLine("Please enter your registration credentials :") }

    }

    @Test
    fun `should throw EmptyFieldException when user input is null`() = runTest {
        val exception = EmptyFieldException()
        every { reader.readInput() } returns null

        registerUi.show()

        handler.printHandledError(exception)
    }

    @Test
    fun `should call registerUser and redirect to login on successful registration`() = runTest {
        every { reader.readInput() } returnsMany listOf("mate", "mate@gmail.com", "mate123")

        registerUi.show()

        coVerify { registerUseCase.registerUser(name = "mate", email = "mate@gmail.com", password = "mate123") }
        coVerify { printer.printCorrectOutput("Register successfully!") }
        coVerify { loginUi.show() }
    }

    @Test
    fun `should print error message when email is already registered`() = runTest {
        val exception = EmailAlreadyExistsException()
        every { reader.readInput() } returnsMany listOf("mate", "mate@gmail.com", "password123")
        coEvery { registerUseCase.registerUser(any(), any(), any()) } throws exception

        registerUi.show()

        coVerify { handler.printHandledError(exception) }
        coVerify(exactly = 0) { loginUi.show() }
    }


}