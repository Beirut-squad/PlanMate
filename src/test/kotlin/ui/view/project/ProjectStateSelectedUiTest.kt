package ui.view.project

import creator_helper.createProjectHelper
import creator_helper.createTaskHelper
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
    fun `should show displays no states message when project has no states`() = runTest {
        // Given
        val project = createProjectHelper(state = emptyList())
        coEvery { getProjectByIdUseCase.getProjectById(projectId) } returns project
        every { printer.readIntInput(any()) } returns null

        // Then
        projectStateSelectedUi.show()

        // When
        verify {
            printer.printInfoLine("No states available for this project.")
        }
    }

    @Test
    fun `should call getProjectStates when project states exist`() = runTest {
        // Given
        val state = State(id = UUID.randomUUID(), name = "To Do")
        val project = createProjectHelper(id = projectId, state = listOf(state))
        coEvery { getProjectByIdUseCase.getProjectById(projectId) } returns project

        // When
        projectStateSelectedUi.show()

        // Then
        coVerify { getProjectByIdUseCase.getProjectById(projectId) }
        verify { printer.printTitle("State Details") }
    }

    @Test
    fun `should handle error when fetching tasks fails`() = runTest {
        // Given
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

        // When
        projectStateSelectedUi.show()

        // Then
        coVerify { exceptionHandler.printHandledError(exception) }
    }

    @Test
    fun `should print state details prints task details when tasks exist`() = runTest {
        // Given
        val stateId = UUID.randomUUID()
        val state = State(id = stateId, name = "In Progress")
        val project = createProjectHelper(id = projectId, state = listOf(state))

        val tasks = listOf(
            createTaskHelper(title = "Task 1", description = "Desc 1", state = state),
            createTaskHelper(title = "Task 2", description = "Desc 2", state = state),
        )

        coEvery { getProjectByIdUseCase.getProjectById(projectId) } returns project
        coEvery {
            getTaskByStateIdAndProjectId.getTaskByStateIdAndProjectId(projectId, stateId)
        } returns tasks
        every { printer.readIntInput(any()) } returns 1

        // When
        projectStateSelectedUi.show()

        // Then
        verify {
            printer.printTitle("State Details")
            printer.printInfoLine("Name: In Progress")
            printer.printInfoLine("Tasks:")
            printer.printInfoLine(" - Name: Task 1, Description: Desc 1")
            printer.printInfoLine(" - Name: Task 2, Description: Desc 2")
        }
    }

    @Test
    fun `should print state details prints no tasks message when no tasks exist`() = runTest {
        // Given
        val stateId = UUID.randomUUID()
        val state = State(id = stateId, name = "Review")
        val project = createProjectHelper(id = projectId, state = listOf(state))

        coEvery { getProjectByIdUseCase.getProjectById(projectId) } returns project
        coEvery {
            getTaskByStateIdAndProjectId.getTaskByStateIdAndProjectId(projectId, stateId)
        } returns emptyList()
        every { printer.readIntInput(any()) } returns 1

        // When
        projectStateSelectedUi.show()

        // Then
        verify {
            printer.printTitle("State Details")
            printer.printInfoLine("Name: Review")
            printer.printInfoLine("No tasks available for this state.")
        }
    }
}