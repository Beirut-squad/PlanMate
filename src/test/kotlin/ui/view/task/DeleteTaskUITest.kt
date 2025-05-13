package ui.view.task

import creator_helper.createTaskHelper
import creator_helper.createUserHelper
import domain.exception.handler.ExceptionHandler
import domain.exception.handler.SafeExecutor
import domain.model.Task
import domain.use_case.authentication.GetCurrentUserUseCase
import domain.use_case.task.DeleteTaskUseCase
import domain.use_case.task.GetProjectTasksUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.dsl.module
import ui.components.Printer
import ui.components.Reader
import ui.view.user.mate.UserProjectsUi
import java.util.*

class DeleteTaskUITest {
    private val printer: Printer = mockk(relaxed = true)
    private val reader: Reader = mockk()
    private val getCurrentUserUseCase: GetCurrentUserUseCase = mockk()
    private val deleteTaskUseCase: DeleteTaskUseCase = mockk(relaxed = true)
    private val getProjectTasksUseCase: GetProjectTasksUseCase = mockk()
    private val exceptionHandler: ExceptionHandler = mockk(relaxed = true)
    private val executor: SafeExecutor = spyk()
    private val projectId = UUID.randomUUID()

    private lateinit var deleteTaskUI: DeleteTaskUI

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
                    single { deleteTaskUseCase }
                    single { getProjectTasksUseCase }
                    single { exceptionHandler }
                    single { executor }
                }
            )
        }
        deleteTaskUI = DeleteTaskUI(projectId)
    }

    @AfterEach
    fun tearDown() {
        stopKoin()
        clearAllMocks()
        unmockkConstructor(UserProjectsUi::class)
    }

    @Test
    fun `should delete task when valid task number and confirmation are provided`() = runTest {
        // Given
        val user = createUserHelper()
        val task1 = createTaskHelper(projectId)
        val task2 = createTaskHelper(projectId)
        val tasks = listOf(task1, task2)

        coEvery { getProjectTasksUseCase.getProjectTasks(projectId) } returns tasks
        coEvery { getCurrentUserUseCase.getCurrentUser() } returns user
        every { reader.readInput() } returnsMany listOf("1", "yes")

        // When
        deleteTaskUI.show()

        // Then
        coVerify {
            deleteTaskUseCase.deleteTask(task1, user.id)
            printer.printInfoLine("Task deleted successfully.")
            anyConstructed<UserProjectsUi>().show()
        }
    }

    @Test
    fun `should cancel deletion when user does not confirm`() = runTest {
        // Given
        val user = createUserHelper()
        val task = createTaskHelper(projectId)
        val tasks = listOf(task)

        coEvery { getProjectTasksUseCase.getProjectTasks(projectId) } returns tasks
        coEvery { getCurrentUserUseCase.getCurrentUser() } returns user
        every { reader.readInput() } returnsMany listOf("1", "no")

        // When
        deleteTaskUI.show()

        // Then
        coVerify(exactly = 0) {
            deleteTaskUseCase.deleteTask(any(), any())
        }
        verify {
            printer.printInfoLine("Deletion cancelled.")
        }
    }
    @Test
    fun `should handle exception when fetching tasks fails`() = runTest {
        // Given
        val exception = RuntimeException("Error fetching tasks")
        coEvery { getProjectTasksUseCase.getProjectTasks(projectId) } throws exception

        // When
        deleteTaskUI.show()

        // Then
        coVerify { exceptionHandler.printHandledError(exception) }

    }
    @Test
    fun `should handle null input for task number`() = runTest {
        // Given
        val user = createUserHelper()
        val task = createTaskHelper(projectId)
        val tasks = listOf(task)

        coEvery { getProjectTasksUseCase.getProjectTasks(projectId) } returns tasks
        coEvery { getCurrentUserUseCase.getCurrentUser() } returns user
        every { reader.readInput() } returnsMany listOf(null, "yes")

        // When
        deleteTaskUI.show()

        // Then
        verify {
            printer.printError("Invalid task number.")
        }
        coVerify(exactly = 0) {
            deleteTaskUseCase.deleteTask(any(), any())
        }
    }

    @Test
    fun `should show error message when no tasks are available`() = runTest {
        // Given
        val tasks = emptyList<Task>()

        coEvery { getProjectTasksUseCase.getProjectTasks(projectId) } returns tasks
        coEvery { getCurrentUserUseCase.getCurrentUser() } returns createUserHelper()

        // When
        deleteTaskUI.show()

        // Then
        verify {
            printer.printInfoLine("No tasks available to delete.")
        }
        coVerify(exactly = 0) {
            deleteTaskUseCase.deleteTask(any(), any())
        }
    }
    @Test
    fun `should show error message when task number is out of bounds`() = runTest {
        // Given
        val user = createUserHelper()
        val task = createTaskHelper(projectId)
        val tasks = listOf(task)

        coEvery { getProjectTasksUseCase.getProjectTasks(projectId) } returns tasks
        coEvery { getCurrentUserUseCase.getCurrentUser() } returns user
        every { reader.readInput() } returnsMany listOf("5", "yes")

        // When
        deleteTaskUI.show()

        // Then
        verify {
            printer.printError("Invalid task number.")
        }
        coVerify(exactly = 0) {
            deleteTaskUseCase.deleteTask(any(), any())
        }
    }

    @Test
    fun `should show error message if exception is thrown during task deletion`() = runTest {
        // Given
        val user = createUserHelper()
        val task = createTaskHelper(projectId)
        val tasks = listOf(task)
        val exception = RuntimeException("Failed to delete task")

        coEvery { getProjectTasksUseCase.getProjectTasks(projectId) } returns tasks
        coEvery { getCurrentUserUseCase.getCurrentUser() } returns user
        every { reader.readInput() } returnsMany listOf("1", "yes")
        coEvery { deleteTaskUseCase.deleteTask(task, user.id) } throws exception

        // When
        deleteTaskUI.show()

        // Then
        coVerify { exceptionHandler.printHandledError(exception) }
    }

    @Test
    fun `should handle empty input for task number`() = runTest {
        // Given
        val user = createUserHelper()
        val task = createTaskHelper(projectId)
        val tasks = listOf(task)

        coEvery { getProjectTasksUseCase.getProjectTasks(projectId) } returns tasks
        coEvery { getCurrentUserUseCase.getCurrentUser() } returns user
        every { reader.readInput() } returnsMany listOf("1",null)

        // When
        deleteTaskUI.show()

        // Then

        verify {
            printer.printInfoLine("Deletion cancelled.")
        }
    }

}