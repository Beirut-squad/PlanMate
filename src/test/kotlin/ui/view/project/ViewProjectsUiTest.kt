package ui.view.project

import creator_helper.createProjectHelper
import domain.exception.handler.ExceptionHandler
import domain.exception.handler.SafeExecutor
import domain.use_case.project.GetAllProjectsUseCase
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import ui.components.Printer
import kotlin.test.Test

class ViewProjectsUiTest {
    private val printer: Printer = mockk(relaxed = true)
    private val getAllProjectsUseCase: GetAllProjectsUseCase = mockk(relaxed = true)
    private val exceptionHandler: ExceptionHandler = mockk(relaxed = true)
    private val executor: SafeExecutor = spyk()
    private lateinit var viewProjectsUi: ViewProjectsUi

    @BeforeEach
    fun setup() {
        viewProjectsUi = ViewProjectsUi(
            printer,
            getAllProjectsUseCase,
            executor,
            exceptionHandler
        )
    }

    @Test
    fun `should display projects when available`() = runTest {
        // Arrange
        val projects = listOf(createProjectHelper())
        coEvery { getAllProjectsUseCase.getAllProjects() } returns projects

        // Act
        viewProjectsUi.show()

        // Assert
        verify { printer.printTitle("Project: ") }
//        verify { printer.printInfoLine(("Test Project")}
    }

    @Test
    fun `should print error when no projects found`() = runTest {
        // Arrange
        coEvery { getAllProjectsUseCase.getAllProjects() } returns emptyList()

        // Act
        viewProjectsUi.show()

        // Assert
        verify { printer.printError("No projects found.") }
    }

    @Test
    fun `should handle exception during fetching`() = runTest {
        // Arrange
        val exception = RuntimeException("Failed to load")
        coEvery { getAllProjectsUseCase.getAllProjects() } throws exception

        // Act
        viewProjectsUi.show()

        // Assert
        verify { exceptionHandler.printHandledError(exception) }
    }
}