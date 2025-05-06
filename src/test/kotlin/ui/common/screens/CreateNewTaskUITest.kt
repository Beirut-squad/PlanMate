package ui.common.screens

import creator_helper.createProjectHelper
import creator_helper.createStateHelper
import creator_helper.createUserHelper
import io.mockk.*
import junit.framework.TestCase.assertEquals
import org.example.logic.use_cases.authentication.GetCurrentLoggedInUserUseCase
import org.example.logic.use_cases.project_manegment.GetProjectByIdUseCase
import org.example.logic.use_cases.task_managemnt.CreateTaskUseCase
import org.example.ui.common.components.Reader
import org.example.ui.common.components.Viewer
import org.example.ui.common.screens.CreateNewTaskUI
import org.example.ui.mate.home_screen.ViewProjectsForUserUI
import org.junit.jupiter.api.BeforeEach
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.dsl.module
import java.util.UUID
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertNotNull

class CreateNewTaskUITest  {
    private var viewer: Viewer = mockk(relaxed = true)
    private var reader: Reader = mockk(relaxed = true)
    private var getCurrentUserUseCase: GetCurrentLoggedInUserUseCase = mockk(relaxed = true)
    private var createTaskUseCase: CreateTaskUseCase = mockk(relaxed = true)
    private val getProjectByIdUseCase: GetProjectByIdUseCase = mockk(relaxed = true)

    private val userId = UUID.randomUUID()
    private val ProjectId = UUID.randomUUID()
    private val user = createUserHelper("mostafa", "testUser", "test@example.com", userId)
    val state = createStateHelper(UUID.randomUUID(), "To Do")
    val project = createProjectHelper(id = ProjectId, state = listOf(state))

    private lateinit var createNewTaskUI: CreateNewTaskUI

    @BeforeEach
    fun setup() {
        stopKoin()

        mockkConstructor(ViewProjectsForUserUI::class)
        every { anyConstructed<ViewProjectsForUserUI>().show() } just Runs


        startKoin {
            modules(module {
                single { viewer }
                single { reader }
                single { getCurrentUserUseCase }
                single { createTaskUseCase }
                single { getProjectByIdUseCase }

            })

        }

        createNewTaskUI = CreateNewTaskUI(ProjectId)
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `should create task when all inputs are valid`() {
        // Given
        every { getProjectByIdUseCase.getProjectById(ProjectId) } returns Result.success(project)
        every { getCurrentUserUseCase.getCurrentUser() } returns Result.success(user)
        every { reader.readInput() } returnsMany listOf("My Task", "My Description", "1")

        // When
        createNewTaskUI.show()

        // Then
        verify {
            createTaskUseCase.createTask(
                "My Task",
                "My Description",
                state,
                ProjectId,
                userId
            )
        }
    }

    @Test
    fun `should show error when project is not found`() {
        // Given
        every { getProjectByIdUseCase.getProjectById(ProjectId) } returns Result.failure(Exception("Project not found"))

        // When
        createNewTaskUI.show()

        // Then
        verify { viewer.printError("Failed to retrieve project: Project not found") }
        verify(exactly = 0) { createTaskUseCase.createTask(any(), any(), any(), any(), any()) }
    }

    @Test
    fun `should show error when current user is not available`() {
        // Given
        every { getProjectByIdUseCase.getProjectById(ProjectId) } returns Result.success(project)
        every { getCurrentUserUseCase.getCurrentUser() } returns Result.failure(Exception("No user"))

        // When
        createNewTaskUI.show()

        // Then
        verify { viewer.printError("No user found") }
        verify(exactly = 0) { createTaskUseCase.createTask(any(), any(), any(), any(), any()) }
    }

    @Test
    fun `should prompt again if task name is blank and then accept valid input`() {
        // Given
        every { getProjectByIdUseCase.getProjectById(ProjectId) } returns Result.success(project)
        every { getCurrentUserUseCase.getCurrentUser() } returns Result.success(user)
        every { reader.readInput() } returnsMany listOf("", "   ", "Valid Task", "Valid Description", "1")

        // When
        createNewTaskUI.show()

        // Then
        verifySequence {
            viewer.printTitle("Let's create a task")
            viewer.printInfoLine("Write your task name:")
            viewer.printError("Input cannot be empty or null. Please try again.")
            viewer.printInfoLine("Write your task name:")
            viewer.printError("Input cannot be empty or null. Please try again.")
            viewer.printInfoLine("Write your task name:")
            viewer.printInfoLine("Tell me more about description of your task:")
            viewer.printOptions("Choose a state for the task:")
            viewer.printInfoLine("1. To Do")
        }

        verify {
            createTaskUseCase.createTask(
                "Valid Task",
                "Valid Description",
                state,
                ProjectId,
                userId
            )
        }
    }

    @Test
    fun `should prompt again if invalid state selected and then accept correct input`() {
        // Given
        every { getProjectByIdUseCase.getProjectById(ProjectId) } returns Result.success(project)
        every { getCurrentUserUseCase.getCurrentUser() } returns Result.success(user)
        every { reader.readInput() } returnsMany listOf("Task", "Desc", "0", "100", "1")

        // When
        createNewTaskUI.show()

        // Then
        verify(atLeast = 2) {
            viewer.printError(match {
                it.contains("Invalid state selection")
            })
        }

        verify {
            createTaskUseCase.createTask("Task", "Desc", state, ProjectId, userId)
        }
    }

    @Test
    fun `should not create task if user enters invalid state multiple times`() {
        // Given
        every { getProjectByIdUseCase.getProjectById(ProjectId) } returns Result.success(project)
        every { getCurrentUserUseCase.getCurrentUser() } returns Result.success(user)
        every { reader.readInput() } returnsMany listOf("Task", "Desc", "", "abc", "10", "1")

        // When
        createNewTaskUI.show()

        // Then
        verify {
            createTaskUseCase.createTask("Task", "Desc", state, ProjectId, userId)
        }
    }

    @Test
    fun `should select valid state when index is within valid range`() {
        // Given
        every { getProjectByIdUseCase.getProjectById(ProjectId) } returns Result.success(project)
        every { getCurrentUserUseCase.getCurrentUser() } returns Result.success(user)
        every { reader.readInput() } returnsMany listOf("Task", "Desc", "1") // Valid index

        // When
        createNewTaskUI.show()

        // Then
        verify {
            createTaskUseCase.createTask(
                "Task",
                "Desc",
                state,
                ProjectId,
                userId
            )
        }
    }
    @Test
    fun `should show error when project retrieval fails`() {
        // Given
        every { getProjectByIdUseCase.getProjectById(ProjectId) } returns Result.failure(Exception("Error retrieving project"))

        // When
        createNewTaskUI.show()

        // Then
        verify { viewer.printError("Failed to retrieve project: Error retrieving project") }
        verify(exactly = 0) { createTaskUseCase.createTask(any(), any(), any(), any(), any()) }
    }
    @Test
    fun `should keep prompting if task name is null or blank`() {
        // Given
        every { getProjectByIdUseCase.getProjectById(ProjectId) } returns Result.success(project)
        every { getCurrentUserUseCase.getCurrentUser() } returns Result.success(user)
        every { reader.readInput() } returnsMany listOf(null, "  ", "Valid Task", "Valid Description", "1")

        // When
        createNewTaskUI.show()

        // Then
        verify(exactly = 2) { viewer.printError("Input cannot be empty or null. Please try again.") }
        verify {
            createTaskUseCase.createTask("Valid Task", "Valid Description", state, ProjectId, userId)
        }
    }
    @Test
    fun `should prompt again for task description if input is invalid`() {
        // Given
        every { getProjectByIdUseCase.getProjectById(ProjectId) } returns Result.success(project)
        every { getCurrentUserUseCase.getCurrentUser() } returns Result.success(user)
        every { reader.readInput() } returnsMany listOf("Valid Task", "", "   ", "Valid Description", "1")

        // When
        createNewTaskUI.show()

        // Then
        verify(exactly = 2) { viewer.printError("Input cannot be empty or null. Please try again.") }
        verify {
            createTaskUseCase.createTask("Valid Task", "Valid Description", state, ProjectId, userId)
        }
    }
    @Test
    fun `should keep prompting until valid state is selected`() {
        // Given
        every { getProjectByIdUseCase.getProjectById(ProjectId) } returns Result.success(project)
        every { getCurrentUserUseCase.getCurrentUser() } returns Result.success(user)
        every { reader.readInput() } returnsMany listOf("Task", "Desc", "invalid", "abc", "0", "1")

        // When
        createNewTaskUI.show()

        // Then
        verify {
            viewer.printError(match { it.contains("Invalid state selection") })
        }
        verify {
            createTaskUseCase.createTask("Task", "Desc", state, ProjectId, userId)
        }
    }
    @Test
    fun `should prompt again for task description if empty`() {
        // Given
        every { getProjectByIdUseCase.getProjectById(ProjectId) } returns Result.success(project)
        every { getCurrentUserUseCase.getCurrentUser() } returns Result.success(user)
        every { reader.readInput() } returnsMany listOf("Valid Task", "", "Valid Description", "1")

        // When
        createNewTaskUI.show()

        // Then
        verify(exactly = 1) { viewer.printError("Input cannot be empty or null. Please try again.") }
        verify {
            createTaskUseCase.createTask("Valid Task", "Valid Description", state, ProjectId, userId)
        }
    }

    @Test
    fun `should not create task if user enters invalid state multiple time`() {
        // Given
        every { getProjectByIdUseCase.getProjectById(ProjectId) } returns Result.success(project)
        every { getCurrentUserUseCase.getCurrentUser() } returns Result.success(user)
        every { reader.readInput() } returnsMany listOf("Task", "Desc", "", "abc", "10", "1")

        // When
        createNewTaskUI.show()

        // Then
        verify {
            createTaskUseCase.createTask("Task", "Desc", state, ProjectId, userId)
        }
    }
    @Test
    fun `should handle null input for state index`() {
        // Given
        every { getProjectByIdUseCase.getProjectById(ProjectId) } returns Result.success(project)
        every { getCurrentUserUseCase.getCurrentUser() } returns Result.success(user)
        every { reader.readInput() } returnsMany listOf("Task Name", "Task Description", null, "1")

        // When
        createNewTaskUI.show()

        // Then
        verify {
            viewer.printError("You must select a state. Please choose a valid number.")
        }
        verify {
            createTaskUseCase.createTask("Task Name", "Task Description", state, ProjectId, userId)
        }
    }

    @Test
    fun `should handle decimal number input for state selection`() {
        // Given
        every { getProjectByIdUseCase.getProjectById(ProjectId) } returns Result.success(project)
        every { getCurrentUserUseCase.getCurrentUser() } returns Result.success(user)
        every { reader.readInput() } returnsMany listOf("Task", "Desc", "1.5", "1")

        // When
        createNewTaskUI.show()

        // Then
        verify {
            viewer.printError(match { it.contains("Invalid state selection") })
        }
        verify {
            createTaskUseCase.createTask("Task", "Desc", state, ProjectId, userId)
        }
    }
    @Test
    fun `should handle null user from getCurrentUser`() {
        // Given
        every { getProjectByIdUseCase.getProjectById(ProjectId) } returns Result.success(project)
        every { getCurrentUserUseCase.getCurrentUser() } returns Result.success(null)

        // When
        createNewTaskUI.show()

        // Then
        verify { viewer.printError("No user found") }
        verify(exactly = 0) { createTaskUseCase.createTask(any(), any(), any(), any(), any()) }
    }
    @Test
    fun `should verify exact sequence when selecting invalid states multiple times`() {
        // Given
        every { getProjectByIdUseCase.getProjectById(ProjectId) } returns Result.success(project)
        every { getCurrentUserUseCase.getCurrentUser() } returns Result.success(user)
        every { reader.readInput() } returnsMany listOf("Task", "Desc", "0", "99", "1")

        // When
        createNewTaskUI.show()

        // Then
        verifySequence {
            viewer.printTitle("Let's create a task")
            viewer.printInfoLine("Write your task name:")
            viewer.printInfoLine("Tell me more about description of your task:")
            viewer.printOptions("Choose a state for the task:")
            viewer.printInfoLine("1. To Do")

            viewer.printError(match { it.contains("Invalid state selection") })

            viewer.printOptions("Choose a state for the task:")
            viewer.printInfoLine("1. To Do")
            viewer.printError(match { it.contains("Invalid state selection") })

            viewer.printOptions("Choose a state for the task:")
            viewer.printInfoLine("1. To Do")
        }

        verify {
            createTaskUseCase.createTask("Task", "Desc", state, ProjectId, userId)
        }
    }

    @Test
    fun `should handle multiple states in project`() {
        // Given
        val state2 = createStateHelper(UUID.randomUUID(), "In Progress")
        val state3 = createStateHelper(UUID.randomUUID(), "Done")
        val projectWithMultipleStates = createProjectHelper(
            id = ProjectId,
            state = listOf(state, state2, state3)
        )

        every { getProjectByIdUseCase.getProjectById(ProjectId) } returns Result.success(projectWithMultipleStates)
        every { getCurrentUserUseCase.getCurrentUser() } returns Result.success(user)
        every { reader.readInput() } returnsMany listOf("Task", "Desc", "2")

        // When
        createNewTaskUI.show()

        // Then
        verify {
            viewer.printInfoLine("1. To Do")
            viewer.printInfoLine("2. In Progress")
            viewer.printInfoLine("3. Done")
        }

        verify {
            createTaskUseCase.createTask("Task", "Desc", state2, ProjectId, userId)
        }
    }
    @Test
    fun `should handle non-numeric input for state selection`() {
        // Given
        every { getProjectByIdUseCase.getProjectById(ProjectId) } returns Result.success(project)
        every { getCurrentUserUseCase.getCurrentUser() } returns Result.success(user)
        every { reader.readInput() } returnsMany listOf("Task", "Desc", "abc", "1")

        // When
        createNewTaskUI.show()

        // Then
        verify {
            viewer.printError(match { it.contains("Invalid state selection") })
        }

        verify {
            createTaskUseCase.createTask("Task", "Desc", state, ProjectId, userId)
        }
    }

    @Test
    fun `should handle multiple null and empty inputs consecutively`() {
        // Given
        every { getProjectByIdUseCase.getProjectById(ProjectId) } returns Result.success(project)
        every { getCurrentUserUseCase.getCurrentUser() } returns Result.success(user)
        every { reader.readInput() } returnsMany listOf(null, "", "   ", "Valid Task", null, "", "Valid Description", "1")

        // When
        createNewTaskUI.show()

        // Then
        verify(exactly = 5) {
            viewer.printError("Input cannot be empty or null. Please try again.")
        }

        verify {
            createTaskUseCase.createTask("Valid Task", "Valid Description", state, ProjectId, userId)
        }
    }
}
