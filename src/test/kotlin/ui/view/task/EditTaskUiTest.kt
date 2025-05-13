package ui.view.task

import creator_helper.createProjectHelper
import creator_helper.createStateHelper
import creator_helper.createTaskHelper
import creator_helper.createUserHelper

import domain.model.Task
import domain.use_case.authentication.GetCurrentUserUseCase
import domain.use_case.project.GetProjectByIdUseCase
import domain.use_case.task.EditTaskUseCase
import domain.use_case.task.GetProjectTasksUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.dsl.module
import ui.common.Printer
import ui.common.Reader
import ui.common.exception.handler.ExceptionHandler
import ui.common.exception.handler.SafeExecutor

import ui.view.user.mate.UserProjectsUi
import java.util.*

class EditTaskUiTest {
    private val printer: Printer = mockk(relaxed = true)
    private val reader: Reader = mockk()
    private val getCurrentUserUseCase: GetCurrentUserUseCase = mockk()
    private val getProjectTasksUseCase: GetProjectTasksUseCase = mockk()
    private val getProjectByIdUseCase: GetProjectByIdUseCase = mockk()
    private val editTaskUseCase: EditTaskUseCase = mockk(relaxed = true)
    private val exceptionHandler: ExceptionHandler = mockk(relaxed = true)
    private val executor: SafeExecutor = spyk()
    private val projectId = UUID.randomUUID()

    private lateinit var editTaskUi: EditTaskUi

    @BeforeEach
    fun setup() {
        mockkConstructor(UserProjectsUi::class)
        coEvery { anyConstructed<UserProjectsUi>().show() } just Runs

        startKoin {
            modules(
                module {
                    single { printer }
                    single { reader }
                    single { getCurrentUserUseCase }
                    single { getProjectTasksUseCase }
                    single { getProjectByIdUseCase }
                    single { editTaskUseCase }
                    single { exceptionHandler }
                    single { executor }
                }
            )
        }
        editTaskUi = EditTaskUi(projectId)
    }

    @AfterEach
    fun tearDown() {
        stopKoin()
        clearAllMocks()
        unmockkConstructor(UserProjectsUi::class)
    }

    @Test
    fun `should edit task when valid inputs are provided`() = runTest {
        // Given
        val user = createUserHelper()
        val state1 = createStateHelper(name = "To Do")
        val state2 = createStateHelper(name = "In Progress")
        val task = createTaskHelper(projectId = projectId, state = state1)
        val tasks = listOf(task)
        val project = createProjectHelper(id = projectId, state = listOf(state1, state2))

        coEvery { getProjectTasksUseCase.getProjectTasks(projectId) } returns tasks
        coEvery { getCurrentUserUseCase.getCurrentUser() } returns user
        coEvery { getProjectByIdUseCase.getProjectById(projectId) } returns project

        every { reader.readInput() } returnsMany listOf("1", "New Title", "New Description", "2")

        // When
        editTaskUi.show()

        // Then
        coVerify {
            editTaskUseCase.editTask(task, "New Title", "New Description", state2, user.id)
            printer.printInfoLine("Task updated successfully!")
            anyConstructed<UserProjectsUi>().show()
        }
    }


    @Test
    fun `should show message when no tasks are available`() = runTest {
        // Given
        val tasks = emptyList<Task>()

        coEvery { getProjectTasksUseCase.getProjectTasks(projectId) } returns tasks
        coEvery { getCurrentUserUseCase.getCurrentUser() } returns createUserHelper()

        // When
        editTaskUi.show()

        // Then
        verify {
            printer.printInfoLine("No tasks available to edit.")
        }
    }

    @Test
    fun `should return to projects screen for invalid task number`() = runTest {
        // Given
        val user = createUserHelper()
        val state = createStateHelper()
        val task = createTaskHelper(projectId = projectId, state = state)
        val tasks = listOf(task)

        coEvery { getProjectTasksUseCase.getProjectTasks(projectId) } returns tasks
        coEvery { getCurrentUserUseCase.getCurrentUser() } returns user

        every { reader.readInput() } returns "99"

        // When
        editTaskUi.show()

        // Then
        verify {
            printer.printError("Invalid input. Returning to the projects screen.")
        }

        coVerify(exactly = 0) {
            getProjectByIdUseCase.getProjectById(any())
        }
    }

    @Test
    fun `should return to projects screen for non-numeric task input`() = runTest {
        // Given
        val user = createUserHelper()
        val state = createStateHelper()
        val task = createTaskHelper(projectId = projectId, state = state)
        val tasks = listOf(task)

        coEvery { getProjectTasksUseCase.getProjectTasks(projectId) } returns tasks
        coEvery { getCurrentUserUseCase.getCurrentUser() } returns user

        // Non-numeric input
        every { reader.readInput() } returns "abc"

        // When
        editTaskUi.show()

        // Then
        verify {
            printer.printError("Invalid input. Returning to the projects screen.")
        }
    }

    @Test
    fun `should handle exception during task fetch`() = runTest {
        // Given
        val exception = RuntimeException("Failed to fetch tasks")
        coEvery { getProjectTasksUseCase.getProjectTasks(projectId) } throws exception

        // When
        editTaskUi.show()

        // Then
        coVerify { exceptionHandler.printHandledError(exception) }
    }

    @Test
    fun `should handle exception during project fetch`() = runTest {
        // Given
        val user = createUserHelper()
        val state = createStateHelper()
        val task = createTaskHelper(projectId = projectId, state = state)
        val tasks = listOf(task)
        val exception = RuntimeException("Failed to fetch project")

        coEvery { getProjectTasksUseCase.getProjectTasks(projectId) } returns tasks
        coEvery { getCurrentUserUseCase.getCurrentUser() } returns user
        coEvery { getProjectByIdUseCase.getProjectById(projectId) } throws exception

        every { reader.readInput() } returns "1"

        // When
        editTaskUi.show()

        // Then
        coVerify { exceptionHandler.printHandledError(exception) }
    }

    @Test
    fun `should handle exception during task edit`() = runTest {
        // Given
        val user = createUserHelper()
        val state = createStateHelper()
        val task = createTaskHelper(projectId = projectId, state = state)
        val tasks = listOf(task)
        val project = createProjectHelper(id = projectId, state = listOf(state))
        val exception = RuntimeException("Failed to edit task")

        coEvery { getProjectTasksUseCase.getProjectTasks(projectId) } returns tasks
        coEvery { getCurrentUserUseCase.getCurrentUser() } returns user
        coEvery { getProjectByIdUseCase.getProjectById(projectId) } returns project
        coEvery { editTaskUseCase.editTask(any(), any(), any(), any(), any()) } throws exception

        every { reader.readInput() } returnsMany listOf("1", "New Title", "New Description", "1")

        // When
        editTaskUi.show()

        // Then
        coVerify { exceptionHandler.printHandledError(exception) }
    }
    @Test
    fun `should accept empty input for title and description`() = runTest {
        // Given
        val user = createUserHelper()
        val state = createStateHelper()
        val task = createTaskHelper(projectId = projectId, state = state, title = "Original Title")
        val tasks = listOf(task)
        val project = createProjectHelper(id = projectId, state = listOf(state))

        coEvery { getProjectTasksUseCase.getProjectTasks(projectId) } returns tasks
        coEvery { getCurrentUserUseCase.getCurrentUser() } returns user
        coEvery { getProjectByIdUseCase.getProjectById(projectId) } returns project

        // Empty inputs for title and description
        every { reader.readInput() } returnsMany listOf("1", "", "", "1")

        // When
        editTaskUi.show()

        // Then
        coVerify {
            editTaskUseCase.editTask(task, "", "", state, user.id)
        }
    }

    @Test
    fun `should change state when valid state number is provided`() = runTest {
        // Given
        val user = createUserHelper()
        val state1 = createStateHelper(name = "To Do")
        val state2 = createStateHelper(name = "In Progress")
        val task = createTaskHelper(projectId = projectId, state = state1)
        val tasks = listOf(task)
        val project = createProjectHelper(id = projectId, state = listOf(state1, state2))

        coEvery { getProjectTasksUseCase.getProjectTasks(projectId) } returns tasks
        coEvery { getCurrentUserUseCase.getCurrentUser() } returns user
        coEvery { getProjectByIdUseCase.getProjectById(projectId) } returns project

        // Keep original title and description, change state
        every { reader.readInput() } returnsMany listOf("1", task.title, task.description, "2")

        // When
        editTaskUi.show()

        // Then
        coVerify {
            editTaskUseCase.editTask(task, task.title, task.description, state2, user.id)
        }
    }
    @Test
    fun `should keep current state when state input is invalid`() = runTest {
        // Given
        val user = createUserHelper()
        val state1 = createStateHelper(name = "To Do")
        val state2 = createStateHelper(name = "In Progress")
        val task = createTaskHelper(projectId = projectId, state = state1)
        val tasks = listOf(task)
        val project = createProjectHelper(id = projectId, state = listOf(state1, state2))

        coEvery { getProjectTasksUseCase.getProjectTasks(projectId) } returns tasks
        coEvery { getCurrentUserUseCase.getCurrentUser() } returns user
        coEvery { getProjectByIdUseCase.getProjectById(projectId) } returns project

        every { reader.readInput() } returnsMany listOf("1", "", "", "99")

        // When
        editTaskUi.show()

        // Then
        coVerify {
            printer.printInfoLine("Keeping the current state: ${state1.name}")
            editTaskUseCase.editTask(task, "", "", state1, user.id)
        }
    }
    @Test
    fun `should keep current state when state input is non-numeric`() = runTest {
        // Given
        val user = createUserHelper()
        val state1 = createStateHelper(name = "To Do")
        val state2 = createStateHelper(name = "In Progress")
        val task = createTaskHelper(projectId = projectId, state = state1)
        val tasks = listOf(task)
        val project = createProjectHelper(id = projectId, state = listOf(state1, state2))

        coEvery { getProjectTasksUseCase.getProjectTasks(projectId) } returns tasks
        coEvery { getCurrentUserUseCase.getCurrentUser() } returns user
        coEvery { getProjectByIdUseCase.getProjectById(projectId) } returns project

        every { reader.readInput() } returnsMany listOf("1", "", "", "abc")

        // When
        editTaskUi.show()

        // Then
        coVerify {
            printer.printInfoLine("Keeping the current state: ${state1.name}")
            editTaskUseCase.editTask(task, "", "", state1, user.id)
        }
    }
    @Test
    fun `should keep current state when state input is null`() = runTest {
        // Given
        val user = createUserHelper()
        val state1 = createStateHelper(name = "To Do")
        val state2 = createStateHelper(name = "In Progress")
        val task = createTaskHelper(projectId = projectId, state = state1)
        val tasks = listOf(task)
        val project = createProjectHelper(id = projectId, state = listOf(state1, state2))

        coEvery { getProjectTasksUseCase.getProjectTasks(projectId) } returns tasks
        coEvery { getCurrentUserUseCase.getCurrentUser() } returns user
        coEvery { getProjectByIdUseCase.getProjectById(projectId) } returns project

        every { reader.readInput() } returnsMany listOf("1", "", "", null)

        // When
        editTaskUi.show()

        // Then
        coVerify {
            printer.printInfoLine("Keeping the current state: ${state1.name}")
            editTaskUseCase.editTask(task, "", "", state1, user.id)
        }
    }

}