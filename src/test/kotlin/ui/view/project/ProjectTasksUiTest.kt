package ui.view.project

import creator_helper.createTaskHelper
import domain.exception.TaskNotFoundException
import domain.exception.handler.ExceptionHandler
import domain.exception.handler.SafeExecutor
import domain.model.State
import domain.use_case.task.GetProjectTasksUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import ui.components.Printer
import ui.components.Reader
import ui.view.task.DeleteTaskUI
import ui.view.task.EditTaskUi
import ui.view.user.mate.UserProjectsUi
import java.util.*

class ProjectTasksUiTest {
    private val printer: Printer = mockk(relaxed = true)
    private val reader: Reader = mockk()
    private val getTasksForProjectUseCase: GetProjectTasksUseCase = mockk(relaxed = true)
    private val exceptionHandler: ExceptionHandler = mockk(relaxed = true)
    private val executor: SafeExecutor = spyk()
    private val projectId = UUID.randomUUID()
    private lateinit var projectTasksUi: ProjectTasksUi


    @BeforeEach
    fun setup() {
        val spyExecutor = spyk(SafeExecutor())
        startKoin {
            modules(
                module {
                    single { printer }
                    single { reader }
                    single { getTasksForProjectUseCase }
                    single { exceptionHandler }
                    single { executor }
                    single { spyExecutor }
                }
            )
        }

        projectTasksUi = ProjectTasksUi(projectId)
    }

    @AfterEach
    fun tearDown() {
        stopKoin()
        clearAllMocks()
    }

    @Test
    fun `should display tasks and navigate to EditTaskUi when user chooses 1`() = runTest {
        // Arrange
        val mockTasks = listOf(
            createTaskHelper(title = "Task 1", description = "", state = State(UUID.randomUUID(), "To Do")),
            createTaskHelper(title = "Task 2", description = "", state = State(UUID.randomUUID(), "In Progress"))
        )
        coEvery { getTasksForProjectUseCase.getProjectTasks(projectId) } returns mockTasks
        every { reader.readInput() } returns "1"

        mockkConstructor(EditTaskUi::class)
        coEvery { anyConstructed<EditTaskUi>().show() } just Runs

        // Act
        projectTasksUi.show()

        // Assert
        verify { printer.printTitle("Tasks for Project:") }
        verify { printer.printOptions("Edit a task", "Delete a task", "Enter Any Thing To Go Back") }
        coVerify { anyConstructed<EditTaskUi>().show() }
    }

    @Test
    fun `should display tasks and navigate to DeleteTaskUI when user chooses 2`() = runTest {
        // Arrange
        val mockTasks = listOf(
            createTaskHelper(title = "Task 1", description = "", state = State(UUID.randomUUID(), "Done"))
        )
        coEvery { getTasksForProjectUseCase.getProjectTasks(projectId) } returns mockTasks
        every { reader.readInput() } returns "2"

        mockkConstructor(DeleteTaskUI::class)
        coEvery { anyConstructed<DeleteTaskUI>().show() } just Runs

        // Act
        projectTasksUi.show()

        // Assert
        coVerify { anyConstructed<DeleteTaskUI>().show() }
    }

    @Test
    fun `should go back to UserProjectsUi on invalid input`() = runTest {
        // Arrange
        val mockTasks = listOf(
            createTaskHelper(title = "Task 1", description = "", state = State(UUID.randomUUID(), "To Do"))
        )
        coEvery { getTasksForProjectUseCase.getProjectTasks(projectId) } returns mockTasks
        every { reader.readInput() } returns "invalid" // non-numeric input

        mockkConstructor(UserProjectsUi::class)
        coEvery { anyConstructed<UserProjectsUi>().show() } just Runs

        // Act
        projectTasksUi.show()

        // Assert
        coVerify{ anyConstructed<UserProjectsUi>().show() }
    }

    @Test
    fun `should handle exception when task fetching fails`() = runTest {
        // Arrange
        val exception = RuntimeException("Something went wrong")
        coEvery { getTasksForProjectUseCase.getProjectTasks(projectId) } throws exception

        // Act
        projectTasksUi.show()

        // Assert
        verify { exceptionHandler.printHandledError(exception) }
    }

    @Test
    fun `handle error when fetching tasks fails`() = runTest {
        // Arrange
        val exception = TaskNotFoundException()

        coEvery {
            getTasksForProjectUseCase.getProjectTasks(projectId)
        } throws exception

        // Act
        projectTasksUi.show()

        // Assert
        coVerify { exceptionHandler.printHandledError(exception) }
    }

    @Test
    fun `should print goodbye and show UserProjectsUi on invalid input`() = runTest {
        val state = State(UUID.randomUUID(), "To Do")
        val task = createTaskHelper(state = state)
        coEvery { getTasksForProjectUseCase.getProjectTasks(projectId) } returns listOf(task)
        every { reader.readInput() } returns "invalid" // Triggers the else branch

        mockkConstructor(UserProjectsUi::class)
        coEvery { anyConstructed<UserProjectsUi>().show() } just Runs

        projectTasksUi.show()

        verify { printer.printGoodbyeMessage("Goodbye") }
        coVerify { anyConstructed<UserProjectsUi>().show() }
    }

    @Test
    fun `should handle null input gracefully and go back`() = runTest {
        val state = State(UUID.randomUUID(), "To Do")
        val task = createTaskHelper(state = state)
        coEvery { getTasksForProjectUseCase.getProjectTasks(projectId) } returns listOf(task)
        every { reader.readInput() } returns null

        mockkConstructor(UserProjectsUi::class)
        coEvery { anyConstructed<UserProjectsUi>().show() } just Runs

        projectTasksUi.show()

        verify { printer.printGoodbyeMessage("Goodbye") }
        coVerify { anyConstructed<UserProjectsUi>().show() }
    }

    @Test
    fun `should print tasks grouped by state in multiple rows`() = runTest {
        val state1 = State(UUID.randomUUID(), "To Do")
        val state2 = State(UUID.randomUUID(), "In Progress")

        val tasks = listOf(
            createTaskHelper(title = "Task A", state = state1),
            createTaskHelper(title = "Task B", state = state1),
            createTaskHelper(title = "Task C", state = state2)
        )

        coEvery { getTasksForProjectUseCase.getProjectTasks(projectId) } returns tasks
        every { reader.readInput() } returns "invalid"

        mockkConstructor(UserProjectsUi::class)
        coEvery { anyConstructed<UserProjectsUi>().show() } just Runs

        projectTasksUi.show()

        verify {
            printer.printInfoLine("To Do                         In Progress                   ")
            printer.printInfoLine("------------------------------------------------------------")
            printer.printInfoLine("Task A                        Task C                        ")
            printer.printInfoLine("Task B                                                      ")
        }
    }
    @Test
    fun `should handle empty task list and print only headers`() = runTest {
        // Arrange
        coEvery { getTasksForProjectUseCase.getProjectTasks(projectId) } returns emptyList()
        every { reader.readInput() } returns "invalid"

        mockkConstructor(UserProjectsUi::class)
        coEvery { anyConstructed<UserProjectsUi>().show() } just Runs

        // Act
        projectTasksUi.show()

        // Assert
        verify { printer.printTitle("Tasks for Project:") }
        // Expect no headers or tasks printed because groupedTasks is empty
        verify { printer.printInfoLine("Please choose an option:") }
        verify { printer.printOptions("Edit a task", "Delete a task", "Enter Any Thing To Go Back") }
        coVerify { anyConstructed<UserProjectsUi>().show() }
    }

}

