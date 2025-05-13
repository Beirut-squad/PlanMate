package ui.view.user.mate

import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import ui.components.Printer
import ui.components.Reader
import kotlin.test.Test

class MateUiTest {

    private val printer: Printer = mockk(relaxed = true)
    private val reader: Reader = mockk()
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
    fun `test show - choose log out option`() = runTest {
        // Arrange: mock the reader input for "Log out" option (option 2)
        every { reader.readInt() } returns 2

        // Act: call the show method
        mateUi.show()

        // Assert: verify interactions with the printer and the goodbye message
        verify { printer.printTitle("Welcome to Plan Mate") }
        verify { printer.printInfoLine("Choose an option:") }
        verify { printer.printOptions("View Projects", "Log out") }
        verify { printer.printGoodbyeMessage("Goodbye") }
    }

    @Test
    fun `should not call user project ui screen when in reader input is wrong`() = runTest {
        // Given
        every { reader.readInt() } returns null

        // When
        mateUi.show()

        // Then
        coVerify(exactly = 0) { userProjectsUI.show() }
    }
}
