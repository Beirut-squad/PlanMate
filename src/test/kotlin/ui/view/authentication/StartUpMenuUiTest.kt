package ui.view.authentication

import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import ui.common.Printer
import ui.common.Reader
import kotlin.test.Test

class StartUpMenuUiTest {
    private val reader: Reader = mockk(relaxed = true)
    private val printer: Printer = mockk(relaxed = true)
    private val registerUi: RegisterUi = mockk(relaxed = true)
    private val loginUi: LoginUi = mockk(relaxed = true)
    private lateinit var startUpMenuUi: StartUpMenuUi

    @BeforeEach
    fun setUp() {
        startUpMenuUi = StartUpMenuUi(reader, printer, registerUi, loginUi)
    }

    @Test
    fun `should print title and options message when show function is called`() = runTest {
        every { reader.readInt() } returns 3

        startUpMenuUi.show()

        verify { printer.printTitle("Welcome to Plan Mate\nwhat would you like to do?") }
        verify {
            printer.printOptions(
                "Register",
                "Login",
                "Exit"
            )
        }
    }

    @Test
    fun `should go to register ui when user enters 1`() = runTest {
        every { reader.readInt() } returnsMany listOf(1, 3)

        startUpMenuUi.show()

        coVerify(exactly = 1) { registerUi.show() }
    }

    @Test
    fun `should go to login ui when user enters 2`() = runTest {
        every { reader.readInt() } returnsMany listOf(2, 3)

        startUpMenuUi.show()

        coVerify(exactly = 1) { loginUi.show() }
    }

    @Test
    fun `should exit and print goodbye message when entering 3`() = runTest {
        every { reader.readInt() } returns 3

        startUpMenuUi.show()

        verify(exactly = 1) { printer.printGoodbyeMessage("See you later!") }
    }


    @Test
    fun `should print invalid option message when entering any other number`() = runTest {
        every { reader.readInt() } returnsMany listOf(4, 3)

        startUpMenuUi.show()

        verify { printer.printError("Invalid option") }
    }

    @Test
    fun `should print invalid option message when input is null`() = runTest {
        every { reader.readInt() } returnsMany listOf(null, 3)

        startUpMenuUi.show()

        verify { printer.printError("Invalid option") }
    }

}