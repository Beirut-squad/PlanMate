package org.example.ui.common.screens

import creator_helper.createStateHelper
import creator_helper.createTaskHelper
import io.mockk.*
import org.example.logic.use_cases.task_managemnt.GetTasksForProjectUseCase
import org.example.models.Task
import org.example.ui.common.components.Reader
import org.example.ui.common.components.Viewer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import java.util.UUID
import kotlin.test.Test

class ViewAllTaskForProjectUITest {
    private var viewer: Viewer = mockk(relaxed = true)
    private var reader: Reader = mockk(relaxed = true)
    private var getTasksForProjectUseCase: GetTasksForProjectUseCase = mockk(relaxed = true)

    private val projectId = UUID.randomUUID()
    private val taskId = UUID.randomUUID()
    private val state = createStateHelper(UUID.randomUUID(), "To Do")
    private val task = createTaskHelper(
        id = taskId,
        title = "Test Task",
        description = "Test Description",
        state = state,
        projectId = projectId
    )

    private lateinit var viewAllTaskForProjectUI: ViewAllTaskForProjectUI

    @BeforeEach
    fun setup() {
        stopKoin()

        mockkConstructor(ViewProjectsForUserUI::class)
        every { anyConstructed<ViewProjectsForUserUI>().show() } just Runs

        mockkConstructor(EditTaskUI::class)
        every { anyConstructed<EditTaskUI>().show() } just runs

        mockkConstructor(DeleteTaskUI::class)
        every { anyConstructed<DeleteTaskUI>().show() } just runs

        startKoin {
            modules(module {
                single { viewer }
                single { reader }
                single { getTasksForProjectUseCase }
            })
        }

        viewAllTaskForProjectUI = ViewAllTaskForProjectUI(projectId)
    }

    @AfterEach
    fun tearDown() {
        stopKoin()
        unmockkAll()
    }

    @Test
    fun `should show message when no tasks are available`() {
        // Given
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(emptyList())

        // When
        viewAllTaskForProjectUI.show()

        // Then
        verify { viewer.printInfoLine("No tasks found for this project.") }
    }

    @Test
    fun `should show error when task retrieval fails`() {
        // Given
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns
                Result.failure(Exception("Failed to get tasks"))

        // When
        viewAllTaskForProjectUI.show()

        // Then
        verify { viewer.printError("An error occurred while retrieving tasks: Failed to get tasks") }
    }

    @Test
    fun `should display tasks and menu options when tasks exist`() {
        // Given
        val tasks = listOf(task)
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { reader.readInput() } returns "3"

        // When
        viewAllTaskForProjectUI.show()

        // Then
        verify { viewer.printGoodbyeMessage("Goodbye") }
    }

    @Test
    fun `should display multiple tasks correctly`() {
        // Given
        val task2 = createTaskHelper(
            id = UUID.randomUUID(),
            title = "Another Task",
            description = "Another Description",
            state = state,
            projectId = projectId
        )
        val tasks = listOf(task, task2)

        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { reader.readInput() } returns "3"

        // When
        viewAllTaskForProjectUI.show()

        // Then
        verify { viewer.printInfoLine(match { it.contains("Task #1") && it.contains("Test Task") }) }
        verify { viewer.printInfoLine(match { it.contains("Task #2") && it.contains("Another Task") }) }
    }

    @Test
    fun `should navigate to edit task screen when option 1 is selected`() {
        // Given
        val tasks = listOf(task)
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { reader.readInput() } returns "1"

        // When
        viewAllTaskForProjectUI.show()

        // Then
        verify { anyConstructed<EditTaskUI>().show() }
    }

    @Test
    fun `should navigate to delete task screen when option 2 is selected`() {
        // Given
        val tasks = listOf(task)
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { reader.readInput() } returns "2"

        // When
        viewAllTaskForProjectUI.show()

        // Then
        verify { anyConstructed<DeleteTaskUI>().show() }
    }

    @Test
    fun `should exit when option 3 is selected`() {
        // Given
        val tasks = listOf(task)
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { reader.readInput() } returns "3"

        // When
        viewAllTaskForProjectUI.show()

        // Then
        verify { viewer.printGoodbyeMessage("Goodbye") }
    }

    @Test
    fun `should show error message when invalid option is selected`() {
        // Given
        val tasks = listOf(task)
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { reader.readInput() } returns "99"

        // When
        viewAllTaskForProjectUI.show()

        // Then
        verify {
            viewer.printGoodbyeMessage("Goodbye")
        }
    }

    @Test
    fun `should handle non-numeric input for menu selection`() {
        // Given
        val tasks = listOf(task)
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { reader.readInput() } returns "abc" // Non-numeric input

        // When
        viewAllTaskForProjectUI.show()

        // Then
        verify {
            viewer.printGoodbyeMessage("Goodbye")
        }
    }

    @Test
    fun `should handle null input for menu selection`() {
        // Given
        val tasks = listOf(task)
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { reader.readInput() } returns null // Null input

        // When
        viewAllTaskForProjectUI.show()

        // Then
        verify {
            viewer.printGoodbyeMessage("Goodbye")
        }
    }

    @Test
    fun `should verify correct constructor arguments when navigating to EditTaskUI`() {
        // Given
        val tasks = listOf(task)
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { reader.readInput() } returns "1"


        // When
        viewAllTaskForProjectUI.show()

        // Then
        verify { EditTaskUI(projectId).show() }
    }

    @Test
    fun `should verify correct constructor arguments when navigating to DeleteTaskUI`() {
        // Given
        val tasks = listOf(task)
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { reader.readInput() } returns "2"


        // When
        viewAllTaskForProjectUI.show()

        // Then
        verify { DeleteTaskUI(projectId).show() }
    }
}