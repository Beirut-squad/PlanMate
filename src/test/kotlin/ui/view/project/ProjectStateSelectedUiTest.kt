package ui.view.project

import creator_helper.createProjectHelper
import domain.exception.TaskNotFoundException
import domain.exception.handler.ExceptionHandler
import domain.exception.handler.SafeExecutor
import domain.model.State
import domain.use_case.project.GetProjectByIdUseCase
import domain.use_case.task.GetTaskByStateIdAndProjectId
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import ui.components.Printer
import java.util.*

class ProjectStateSelectedUiTest {
    private val printer: Printer = mockk(relaxed = true)
    private val getProjectByIdUseCase: GetProjectByIdUseCase = mockk()
    private val getTaskByStateIdAndProjectId: GetTaskByStateIdAndProjectId = mockk()
    private val executor: SafeExecutor = spyk()
    private val exceptionHandler: ExceptionHandler = mockk(relaxed = true)
    private val projectId = UUID.randomUUID()
    private lateinit var projectStateSelectedUi: ProjectStateSelectedUi

    @BeforeEach
    fun setup() {
        startKoin {
            modules(
                module {
                    single { printer }
                    single { getProjectByIdUseCase }
                    single { getTaskByStateIdAndProjectId }
                    single { executor }
                    single { exceptionHandler }
                }
            )
        }

        projectStateSelectedUi = ProjectStateSelectedUi(projectId)
    }

    @AfterEach
    fun tearDown() {
        stopKoin()
        clearAllMocks()
    }

    @Test
    fun `show displays no states message when project has no states`() = runTest {
        // Arrange
        val project = createProjectHelper(state = emptyList())
        coEvery { getProjectByIdUseCase.getProjectById(projectId) } returns project
        every { printer.readIntInput(any()) } returns null

        // Act
        projectStateSelectedUi.show()

        // Assert
        verify {
            printer.printInfoLine("No states available for this project.")
        }
    }

    @Test
    fun `getProjectStates returns project states`() = runTest {
        // Arrange
        val state = State(id = UUID.randomUUID(), name = "To Do")
        val project = createProjectHelper(id = projectId, state = listOf(state))
        coEvery { getProjectByIdUseCase.getProjectById(projectId) } returns project

        // Act
        projectStateSelectedUi.show()

        // Assert
        coVerify { getProjectByIdUseCase.getProjectById(projectId) }
        verify { printer.printTitle("State Details") }
    }

    @Test
    fun `handle error when fetching tasks fails`() = runTest {
        // Arrange
        val exception = TaskNotFoundException()
        val state = State(id = UUID.randomUUID(), name = "To Do")
        val project = createProjectHelper(id = projectId, state = listOf(state))

        coEvery {
            getProjectByIdUseCase.getProjectById(project.id)
        } returns project

        coEvery {
            getTaskByStateIdAndProjectId.getTaskByStateIdAndProjectId(project.id, state.id)
        } throws exception

        every { exceptionHandler.printHandledError(exception) } just Runs

        every { printer.readIntInput(any()) } returns 1

        // Act
        projectStateSelectedUi.show()

        // Assert
        coVerify { exceptionHandler.printHandledError(exception) }
    }

}