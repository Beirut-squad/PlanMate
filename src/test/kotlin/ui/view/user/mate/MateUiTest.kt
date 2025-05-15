package ui.view.user.mate

import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import ui.common.Printer
import ui.common.Reader
import kotlin.test.Test

class MateUiTest {

    private val printer: Printer = mockk(relaxed = true)
    private val reader: Reader = mockk(relaxed = true)
    private val userProjectsUI: UserProjectsUi = mockk(relaxed = true)

    private val mateUi = MateUi()

    @BeforeEach
    fun setup() {
        startKoin {
            modules(
                module {
                    single { printer }
                    single { reader }
                    single { userProjectsUI }
                }
            )
        }
    }

    @AfterEach
    fun tearDown() {
        stopKoin()
        clearAllMocks()
    }

    @Test
    fun `should display choose view projects option`() = runTest {
        // Given
        every { reader.readInt() } returns 1 andThen 2

        // When
        mateUi.show()

        // Then
        verify { printer.printTitle("Welcome to Plan Mate") }
        verify { printer.printInfoLine("Choose an option:") }
        verify { printer.printOptions("View Projects", "Log out") }
        coVerify { userProjectsUI.show() }
    }

    @Test
    fun `should logout and return to the startup menu when chooses logout option`() = runTest {
        // Given
        every { reader.readInt() } returns 2

        // When
        mateUi.show()

        // Then
        coVerify(exactly = 0) { userProjectsUI.show() }
        verify { printer.printTitle("Welcome to Plan Mate") }
        verify { printer.printOptions("View Projects", "Log out") }
    }

    @Test
    fun `should not call user project ui screen and print error message when choose invalid option`() = runTest {
        // Given
        every { reader.readInt() } returnsMany listOf(20, 2)

        // When
        mateUi.show()

        // Then
        verify { printer.printError("Invalid option") }
        coVerify(exactly = 0) { userProjectsUI.show() }
    }

    @Test
    fun `should not call user project ui screen and print invalid option message when input is null`() = runTest {
        // Given
        every { reader.readInt() } returnsMany listOf(null, 2)

        // When
        mateUi.show()

        // Then
        verify { printer.printError("Invalid option") }
        coVerify(exactly = 0) { userProjectsUI.show() }
    }
}
