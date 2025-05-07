package org.example.ui.common.screens

import creator_helper.createProjectHelper
import creator_helper.createStateHelper
import creator_helper.createTaskHelper
import io.mockk.*
import org.example.logic.use_cases.task_managemnt.DeleteTaskUseCase
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

class DeleteTaskUITest {
    private var viewer: Viewer = mockk(relaxed = true)
    private var reader: Reader = mockk(relaxed = true)
    private var getTasksForProjectUseCase: GetTasksForProjectUseCase = mockk(relaxed = true)
    private var deleteTaskUseCase: DeleteTaskUseCase = mockk(relaxed = true)

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

    private lateinit var deleteTaskUI: DeleteTaskUI

    @BeforeEach
    fun setup() {
        stopKoin()

        startKoin {
            modules(module {
                single { viewer }
                single { reader }
                single { getTasksForProjectUseCase }
                single { deleteTaskUseCase }
            })
        }

        deleteTaskUI = DeleteTaskUI(projectId)
    }

    @AfterEach
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `should show message when no tasks are available`() {
        // Given
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(emptyList())

        // When
        deleteTaskUI.show()

        // Then
        verify { viewer.printInfoLine("No tasks available to delete.") }
        verify(exactly = 0) { deleteTaskUseCase.deleteTask(any(), any()) }
    }

    @Test
    fun `should show error when task retrieval fails`() {
        // Given
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns
                Result.failure(Exception("Failed to get tasks"))

        // When
        deleteTaskUI.show()

        // Then
        verify { viewer.printError("Failed to retrieve tasks: Failed to get tasks") }
        verify(exactly = 0) { deleteTaskUseCase.deleteTask(any(), any()) }
    }

    @Test
    fun `should display tasks and handle invalid task selection`() {
        // Given
        val tasks = listOf(task)
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { reader.readInput() } returns "0" // Invalid index

        // When
        deleteTaskUI.show()

        // Then
        verify { viewer.printTitle("Select a Task to Delete:") }
        verify { viewer.printInfoLine(match { it.contains("Task #1") }) }
        verify { viewer.printLoader("Enter the task number to delete:") }
        verify { viewer.printError("Invalid task number.") }
        verify(exactly = 0) { deleteTaskUseCase.deleteTask(any(), any()) }
    }

    @Test
    fun `should handle null task selection input`() {
        // Given
        val tasks = listOf(task)
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { reader.readInput() } returns null

        // When
        deleteTaskUI.show()

        // Then
        verify { viewer.printError("Invalid task number.") }
        verify(exactly = 0) { deleteTaskUseCase.deleteTask(any(), any()) }
    }

    @Test
    fun `should handle non-numeric task selection input`() {
        // Given
        val tasks = listOf(task)
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { reader.readInput() } returns "abc"

        // When
        deleteTaskUI.show()

        // Then
        verify { viewer.printError("Invalid task number.") }
        verify(exactly = 0) { deleteTaskUseCase.deleteTask(any(), any()) }
    }

    @Test
    fun `should handle out of bounds task selection`() {
        // Given
        val tasks = listOf(task)
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { reader.readInput() } returns "99" // Out of bounds

        // When
        deleteTaskUI.show()

        // Then
        verify { viewer.printError("Invalid task number.") }
        verify(exactly = 0) { deleteTaskUseCase.deleteTask(any(), any()) }
    }

    @Test
    fun `should confirm and delete task when user confirms with yes`() {
        // Given
        val tasks = listOf(task)
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { reader.readInput() } returnsMany listOf("1", "yes")
        every { deleteTaskUseCase.deleteTask(task, taskId) } just runs

        // When
        deleteTaskUI.show()

        // Then
        verify { viewer.printInfoLine("Are you sure you want to delete the task: 'Test Task'? (yes/no)") }
        verify { deleteTaskUseCase.deleteTask(task, taskId) }
        verify { viewer.printInfoLine("Task deleted successfully.") }
    }

    @Test
    fun `should handle confirmation with YES (uppercase)`() {
        // Given
        val tasks = listOf(task)
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { reader.readInput() } returnsMany listOf("1", "YES")
        every { deleteTaskUseCase.deleteTask(task, taskId) } just runs

        // When
        deleteTaskUI.show()

        // Then
        verify { deleteTaskUseCase.deleteTask(task, taskId) }
        verify { viewer.printInfoLine("Task deleted successfully.") }
    }

    @Test
    fun `should not delete task when user cancels with no`() {
        // Given
        val tasks = listOf(task)
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { reader.readInput() } returnsMany listOf("1", "no")

        // When
        deleteTaskUI.show()

        // Then
        verify { viewer.printInfoLine("Deletion cancelled.") }
        verify(exactly = 0) { deleteTaskUseCase.deleteTask(any(), any()) }
    }

    @Test
    fun `should not delete task when user cancels with anything other than yes`() {
        // Given
        val tasks = listOf(task)
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { reader.readInput() } returnsMany listOf("1", "cancel")

        // When
        deleteTaskUI.show()

        // Then
        verify { viewer.printInfoLine("Deletion cancelled.") }
        verify(exactly = 0) { deleteTaskUseCase.deleteTask(any(), any()) }
    }

    @Test
    fun `should handle error during task deletion`() {
        // Given
        val tasks = listOf(task)
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { reader.readInput() } returnsMany listOf("1", "yes")
        every { deleteTaskUseCase.deleteTask(task, taskId) } throws Exception("Deletion failed")

        // When
        deleteTaskUI.show()

        // Then
        verify { viewer.printError("Failed to delete task: Deletion failed") }
    }

    @Test
    fun `should correctly display multiple tasks`() {
        // Given
        val task2 = createTaskHelper(
            UUID.randomUUID(),
            projectId,
            "Another Task",
            "Another Description",
            state,
            UUID.randomUUID()
        )

        val tasks = listOf(task, task2)
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { reader.readInput() } returnsMany listOf("2", "no")

        // When
        deleteTaskUI.show()

        // Then
        verifySequence {
            viewer.printTitle("Select a Task to Delete:")
            viewer.printInfoLine(match { it.contains("Task #1") && it.contains("Test Task") })
            viewer.printInfoLine(match { it.contains("Task #2") && it.contains("Another Task") })
            viewer.printLoader("Enter the task number to delete:")
            viewer.printInfoLine("Are you sure you want to delete the task: 'Another Task'? (yes/no)")
            viewer.printInfoLine("Deletion cancelled.")
        }
    }
}