package ui.view.task

import creator_helper.createProjectHelper
import creator_helper.createStateHelper
import creator_helper.createUserHelper
import domain.exception.handler.ExceptionHandler
import domain.exception.handler.SafeExecutor
import domain.use_case.authentication.GetCurrentUserUseCase
import domain.use_case.project.GetProjectByIdUseCase
import domain.use_case.task.CreateTaskUseCase
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
import java.util.*

class CreateTaskUiTest {
    private val printer: Printer = mockk(relaxed = true)
    private val reader: Reader = mockk()
    private val getCurrentUserUseCase: GetCurrentUserUseCase = mockk()
    private val createTaskUseCase: CreateTaskUseCase = mockk(relaxed = true)
    private val getProjectByIdUseCase: GetProjectByIdUseCase = mockk()
    private val exceptionHandler: ExceptionHandler = mockk(relaxed = true)
    private val executor: SafeExecutor = spyk()
    private val projectId = UUID.randomUUID()

    private lateinit var createTaskUi: CreateTaskUi

    @BeforeEach
    fun setup() {
        startKoin {
            modules(
                module {
                    single { printer }
                    single { reader }
                    single { getCurrentUserUseCase }
                    single { createTaskUseCase }
                    single { getProjectByIdUseCase }
                    single { exceptionHandler }
                    single { executor }
                }
            )
        }
        createTaskUi = CreateTaskUi(projectId)
    }

    @AfterEach
    fun tearDown() {
        stopKoin()
        clearAllMocks()
    }

    @Test
    fun `should create task when valid input is provided`() = runTest {
        // Given
        val user = createUserHelper()
        val state = createStateHelper()
        val project = createProjectHelper(state = listOf(state))

        coEvery { getProjectByIdUseCase.getProjectById(projectId) } returns project
        coEvery { getCurrentUserUseCase.getCurrentUser() } returns user
        every { reader.readInput() } returnsMany listOf("Task Title", "Task Desc", "1")

        // When
        createTaskUi.show()

        // Then
        coVerify {
            createTaskUseCase.createTask("Task Title", "Task Desc", state, project.id, user.id)
        }
    }

    @Test
    fun `should retry input if task name is blank`() = runTest {
        // Given
        val user = createUserHelper()
        val state = createStateHelper()
        val project = createProjectHelper(state = listOf(state))

        coEvery { getProjectByIdUseCase.getProjectById(projectId) } returns project
        coEvery { getCurrentUserUseCase.getCurrentUser() } returns user

        every { reader.readInput() } returnsMany listOf(" ", "Valid Name", "Valid Desc", "1")

        // When
        createTaskUi.show()

        // Then
        verify {
            printer.printError("Input cannot be empty or null. Please try again.")
        }
    }

    @Test
    fun `should retry input if description is blank`() = runTest {
        // Given
        val user = createUserHelper()
        val state = createStateHelper()
        val project = createProjectHelper(state = listOf(state))

        coEvery { getProjectByIdUseCase.getProjectById(projectId) } returns project
        coEvery { getCurrentUserUseCase.getCurrentUser() } returns user

        every { reader.readInput() } returnsMany listOf("Valid Name", "", "Valid Desc", "1")

        // When
        createTaskUi.show()

        // Then
        verify {
            printer.printError("Input cannot be empty or null. Please try again.")
        }
    }

    @Test
    fun `should handle invalid state selection`() = runTest {
        // Given
        val user = createUserHelper()
        val state = createStateHelper()
        val project = createProjectHelper(state = listOf(state))

        coEvery { getProjectByIdUseCase.getProjectById(projectId) } returns project
        coEvery { getCurrentUserUseCase.getCurrentUser() } returns user

        every { reader.readInput() } returnsMany listOf("Valid Name", "Valid Desc", "abc", "1")

        // When
        createTaskUi.show()

        // Then
        verify {
            printer.printError("Invalid state selection. Please choose a valid number between 1 and 1.")
        }
        coVerify {
            createTaskUseCase.createTask("Valid Name", "Valid Desc", state, project.id, user.id)
        }
    }

    @Test
    fun `should show error message if exception is thrown`() = runTest {
        // Given
        val exception = RuntimeException("task creation failed")
        coEvery { getProjectByIdUseCase.getProjectById(projectId) } throws exception

        // When
        createTaskUi.show()

        // Then
        coVerify { exceptionHandler.printHandledError(exception) }
    }



    @Test
    fun `should show error when state input is blank and then accept valid input`() = runTest {
        // Given
        val project = createProjectHelper()
        val user = createUserHelper()

        coEvery { getProjectByIdUseCase.getProjectById(any()) } returns project
        coEvery { getCurrentUserUseCase.getCurrentUser() } returns user

        every { reader.readInput() } returnsMany listOf("test", "test", " ", "1")
        coEvery { createTaskUseCase.createTask(any(), any(), any(), any(), any()) } just Runs

        val createTaskUi = CreateTaskUi(project.id)

        // Given
        createTaskUi.show()

        // Given
        verify {
            printer.printError("You must select a state. Please choose a valid number.")
        }
    }
    @Test
    fun `should handle negative number for state selection`() = runTest {
        // Given
        val user = createUserHelper()
        val state = createStateHelper()
        val project = createProjectHelper(state = listOf(state))

        coEvery { getProjectByIdUseCase.getProjectById(projectId) } returns project
        coEvery { getCurrentUserUseCase.getCurrentUser() } returns user

        every { reader.readInput() } returnsMany listOf("Valid Name", "Valid Desc", "-1", "1")

        // When
        createTaskUi.show()

        // Then
        verify {
            printer.printError("Invalid state selection. Please choose a valid number between 1 and 1.")
        }
        coVerify {
            createTaskUseCase.createTask("Valid Name", "Valid Desc", state, project.id, user.id)
        }
    }

    @Test
    fun `should handle null state input followed by valid input`() = runTest {
        // Given
        val user = createUserHelper()
        val state = createStateHelper()
        val project = createProjectHelper(state = listOf(state))

        coEvery { getProjectByIdUseCase.getProjectById(projectId) } returns project
        coEvery { getCurrentUserUseCase.getCurrentUser() } returns user

        every { reader.readInput() } returnsMany listOf("Valid Name", "Valid Desc", null, "1")

        // When
        createTaskUi.show()

        // Then
        verify {
            printer.printError("You must select a state. Please choose a valid number.")
        }
        coVerify {
            createTaskUseCase.createTask("Valid Name", "Valid Desc", state, project.id, user.id)
        }
    }
    @Test
    fun `should handle null input for task name`() = runTest {
        // Given
        val user = createUserHelper()
        val state = createStateHelper()
        val project = createProjectHelper(state = listOf(state))

        coEvery { getProjectByIdUseCase.getProjectById(projectId) } returns project
        coEvery { getCurrentUserUseCase.getCurrentUser() } returns user

        every { reader.readInput() } returnsMany listOf(null, "Valid Name", "Valid Desc", "1")

        // When
        createTaskUi.show()

        // Then
        verify {
            printer.printError("Input cannot be empty or null. Please try again.")
        }
        coVerify {
            createTaskUseCase.createTask("Valid Name", "Valid Desc", state, project.id, user.id)
        }
    }
}