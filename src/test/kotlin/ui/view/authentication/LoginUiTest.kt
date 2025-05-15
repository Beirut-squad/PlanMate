package ui.view.authentication

import creator_helper.adminUser
import creator_helper.mateUser
import ui.common.exception.EmptyFieldException
import ui.common.exception.InvalidCredentialsException
import ui.common.exception.handler.ExceptionHandler
import ui.common.exception.handler.SafeExecutor
import domain.useCase.authentication.LoginUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import ui.common.Printer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.assertThrows
import ui.common.Reader
import ui.common.Validator
import ui.view.user.admin.home.AdminUi
import ui.view.user.mate.MateUi
import kotlin.test.Test

class LoginUiTest {

    private val reader: Reader = mockk(relaxed = true)
    private val printer: Printer = mockk(relaxed = true)
    private val loginUseCase: LoginUseCase = mockk(relaxed = true)
    private val adminUi: AdminUi = mockk(relaxed = true)
    private val mateUi: MateUi = mockk(relaxed = true)
    private val executor: SafeExecutor = spyk()
    private val handler: ExceptionHandler = mockk(relaxed = true)
    private val validator: Validator = mockk(relaxed = true)

    private lateinit var loginUi: LoginUi

    @BeforeEach
    fun setUp() {
        loginUi = LoginUi(
            reader,
            printer,
            loginUseCase,
            adminUi,
            mateUi,
            executor,
            handler,
            validator
        )
    }

    @Test
    fun `should print title and prompt messages when show function is called`() = runTest {
        loginUi.show()

        verify { printer.printTitle("Login for Plan Mate") }
        verify { printer.printInfoLine("Please enter your information to login:") }
    }

    @Disabled
    @Test
    fun `should throw EmptyFieldException when user input is null`() = runTest {
        every { reader.readInput() } returns null

        assertThrows<EmptyFieldException> {
            loginUi.show()
        }
    }

    @Test
    fun `should login and navigate to admin screen for admin user`() = runTest {
        every { reader.readInput() } returnsMany listOf("admin@gmail.com", "admin123")
        coEvery { loginUseCase.login(adminUser.email, adminUser.password) } returns adminUser

        loginUi.show()

        coVerify { adminUi.show() }
        verify { printer.printCorrectOutput("Welcome ${adminUser.name}") }
        verify { printer.printCorrectOutput("Login successful!") }
    }


    @Test
    fun `should login and navigate to mate screen for mate user`() = runTest {

        every { reader.readInput() } returnsMany listOf("mate@gmail.com", "mate123")
        coEvery { loginUseCase.login(mateUser.email, mateUser.password) } returns mateUser

        loginUi.show()

        coVerify { mateUi.show() }
        verify { printer.printCorrectOutput("Welcome ${mateUser.name}") }
        verify { printer.printCorrectOutput("Login successful!") }
    }

    @Test
    fun `should print invalid credentials message when wrong email or password is entered`() = runTest {
        val exception = InvalidCredentialsException()
        coEvery { loginUseCase.login(any(), any()) } throws exception

        loginUi.show()

        verify { handler.printHandledError(exception) }
    }

}


