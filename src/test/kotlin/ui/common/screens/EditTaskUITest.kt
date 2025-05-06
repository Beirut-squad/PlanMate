package org.example.ui.common.screens

import com.google.common.truth.ExpectFailure.assertThat
import creator_helper.createProjectHelper
import creator_helper.createStateHelper
import creator_helper.createTaskHelper
import io.mockk.*
import org.example.logic.use_cases.project_manegment.GetProjectByIdUseCase
import org.example.logic.use_cases.task_managemnt.EditTaskUseCase
import org.example.logic.use_cases.task_managemnt.GetTasksForProjectUseCase
import org.example.models.Project
import org.example.models.State
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

class EditTaskUITest {
    private var viewer: Viewer = mockk(relaxed = true)
    private var reader: Reader = mockk(relaxed = true)
    private var getTasksForProjectUseCase: GetTasksForProjectUseCase = mockk(relaxed = true)
    private var getProjectByIdUseCase: GetProjectByIdUseCase = mockk(relaxed = true)
    private var editTaskUseCase: EditTaskUseCase = mockk(relaxed = true)

    private val projectId = UUID.randomUUID()
    private val taskId = UUID.randomUUID()

    private val toDoStateId = UUID.randomUUID()
    private val toDoState = createStateHelper(toDoStateId, "To Do")
    private val inProgressStateId = UUID.randomUUID()
    private val inProgressState = createStateHelper(inProgressStateId, "In Progress")
    private val doneStateId = UUID.randomUUID()
    private val doneState = createStateHelper(doneStateId, "Done")


    private val project = createProjectHelper(
        id = projectId,
        name = "Test Project",
        state = listOf(toDoState, inProgressState, doneState)
    )

    private val task = createTaskHelper(
        id = taskId,
        title = "Test Task",
        description = "Test Description",
        state = toDoState,
        projectId = projectId
    )

    private lateinit var editTaskUI: EditTaskUI

    @BeforeEach
    fun setup() {
        stopKoin()

        startKoin {
            modules(module {
                single { viewer }
                single { reader }
                single { getTasksForProjectUseCase }
                single { getProjectByIdUseCase }
                single { editTaskUseCase }
            })
        }

        editTaskUI = EditTaskUI(projectId)
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
        editTaskUI.show()

        // Then
        verify { viewer.printInfoLine("No tasks available to edit.") }
        verify(exactly = 0) { getProjectByIdUseCase.getProjectById(any()) }
        verify(exactly = 0) { editTaskUseCase.editTask(any(), any(), any(), any()) }
    }

    @Test
    fun `should show error when task retrieval fails`() {
        // Given
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns
                Result.failure(Exception("Failed to get tasks"))

        // When
        editTaskUI.show()

        // Then
        verify { viewer.printError("Failed to retrieve tasks: Failed to get tasks") }
        verify(exactly = 0) { getProjectByIdUseCase.getProjectById(any()) }
        verify(exactly = 0) { editTaskUseCase.editTask(any(), any(), any(), any()) }
    }

    @Test
    fun `should display tasks and handle invalid task selection`() {
        // Given
        val tasks = listOf(task)
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { reader.readInput() } returns "0" // Invalid index

        // When
        editTaskUI.show()

        // Then
        verify { viewer.printTitle("Select a Task to Edit:") }
        verify { viewer.printInfoLine(match { it.contains("Task #1") && it.contains("Test Task") }) }
        verify { viewer.printLoader("Enter the task number to edit:") }
        verify { viewer.printError("Invalid task number.") }
        verify(exactly = 0) { getProjectByIdUseCase.getProjectById(any()) }
        verify(exactly = 0) { editTaskUseCase.editTask(any(), any(), any(), any()) }
    }

    @Test
    fun `should handle null task selection input`() {
        // Given
        val tasks = listOf(task)
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { reader.readInput() } returns null

        // When
        editTaskUI.show()

        // Then
        verify { viewer.printError("Invalid task number.") }
        verify(exactly = 0) { getProjectByIdUseCase.getProjectById(any()) }
        verify(exactly = 0) { editTaskUseCase.editTask(any(), any(), any(), any()) }
    }

    @Test
    fun `should handle non-numeric task selection input`() {
        // Given
        val tasks = listOf(task)
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { reader.readInput() } returns "abc"

        // When
        editTaskUI.show()

        // Then
        verify { viewer.printError("Invalid task number.") }
        verify(exactly = 0) { getProjectByIdUseCase.getProjectById(any()) }
        verify(exactly = 0) { editTaskUseCase.editTask(any(), any(), any(), any()) }
    }

    @Test
    fun `should handle out of bounds task selection`() {
        // Given
        val tasks = listOf(task)
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { reader.readInput() } returns "99" // Out of bounds

        // When
        editTaskUI.show()

        // Then
        verify { viewer.printError("Invalid task number.") }
        verify(exactly = 0) { getProjectByIdUseCase.getProjectById(any()) }
        verify(exactly = 0) { editTaskUseCase.editTask(any(), any(), any(), any()) }
    }

    @Test
    fun `should show error when project retrieval fails`() {
        // Given
        val tasks = listOf(task)
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { reader.readInput() } returns "1"
        every { getProjectByIdUseCase.getProjectById(projectId) } returns
                Result.failure(Exception("Failed to get project"))

        // When
        editTaskUI.show()

        // Then
        verify { viewer.printError("Failed to retrieve project: Failed to get project") }
        verify(exactly = 0) { editTaskUseCase.editTask(any(), any(), any(), any()) }
    }

    @Test
    fun `should keep existing values when input is empty`() {
        // Given
        val tasks = listOf(task)
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { getProjectByIdUseCase.getProjectById(projectId) } returns Result.success(project)
        every { reader.readInput() } returnsMany listOf(
            "1",
            "",
            "",
            "invalid"
        )
        every { editTaskUseCase.editTask(task, task.title, task.description, task.state) } just runs

        // When
        editTaskUI.show()

        // Then
        verify { viewer.printTitle("Edit Task - Test Task") }
        verify { viewer.printLoader(match { it.contains("Enter new Title") }) }
        verify { viewer.printLoader(match { it.contains("Enter new Description") }) }
        verify { viewer.printOptions("Choose a state for the task (Current: To Do):") }
        verify { viewer.printInfoLine("Keeping the current state: To Do") }
        verify { editTaskUseCase.editTask(task, task.title, task.description, task.state) }
        verify { viewer.printInfoLine("Task updated successfully!") }
    }

    @Test
    fun `should update task with new values`() {
        // Given
        val tasks = listOf(task)
        val newTitle = "Updated Task Title"
        val newDescription = "Updated Task Description"

        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { getProjectByIdUseCase.getProjectById(projectId) } returns Result.success(project)
        every { reader.readInput() } returnsMany listOf(
            "1",
            newTitle,
            newDescription,
            "2"
        )
        every { editTaskUseCase.editTask(task, newTitle, newDescription, inProgressState) } just runs

        // When
        editTaskUI.show()

        // Then
        verify { viewer.printTitle("Edit Task - Test Task") }
        verify { editTaskUseCase.editTask(task, newTitle, newDescription, inProgressState) }
        verify { viewer.printInfoLine("Task updated successfully!") }
    }

    @Test
    fun `should handle exception during task update`() {
        // Given
        val tasks = listOf(task)
        val newTitle = "Updated Task Title"
        val newDescription = "Updated Task Description"

        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { getProjectByIdUseCase.getProjectById(projectId) } returns Result.success(project)
        every { reader.readInput() } returnsMany listOf(
            "1",
            newTitle,
            newDescription,
            "2"
        )
        every { editTaskUseCase.editTask(task, newTitle, newDescription, inProgressState) } throws
                Exception("Update failed")

        // When
        editTaskUI.show()

        // Then
        verify { viewer.printError("Failed to update task: Update failed") }
    }

    @Test
    fun `should display all available states for selection`() {
        // Given
        val tasks = listOf(task)

        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { getProjectByIdUseCase.getProjectById(projectId) } returns Result.success(project)
        every { reader.readInput() } returnsMany listOf(
            "1",
            "",
            "",
            "3"
        )
        every { editTaskUseCase.editTask(task, task.title, task.description, doneState) } just runs

        // When
        editTaskUI.show()

        // Then
        verify { viewer.printOptions("Choose a state for the task (Current: To Do):") }

        verify { viewer.printInfoLine("1. To Do") }
        verify { viewer.printInfoLine("2. In Progress") }
        verify { viewer.printInfoLine("3. Done") }

        verify { editTaskUseCase.editTask(task, task.title, task.description, doneState) }
    }

    @Test
    fun `should correctly display multiple tasks`() {
        // Given
        val task2 = createTaskHelper(
            UUID.randomUUID(),
            projectId,
            "Another Task",
            "Another Description",
            inProgressState,
            UUID.randomUUID()
        )

        val tasks = listOf(task, task2)
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { getProjectByIdUseCase.getProjectById(projectId) } returns Result.success(project)
        every { reader.readInput() } returnsMany listOf(
            "2",
            "",
            "",
            "0"
        )
        every { editTaskUseCase.editTask(task2, task2.title, task2.description, task2.state) } just runs

        // When
        editTaskUI.show()

        // Then
        verify { viewer.printTitle("Select a Task to Edit:") }
        verify { viewer.printInfoLine(match { it.contains("Task #1") && it.contains("Test Task") }) }
        verify { viewer.printInfoLine(match { it.contains("Task #2") && it.contains("Another Task") }) }

        verify { viewer.printTitle("Edit Task - Another Task") }
        verify { editTaskUseCase.editTask(task2, task2.title, task2.description, task2.state) }
    }

    @Test
    fun `should handle blank input after trim`() {
        // Given
        val tasks = listOf(task)
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { reader.readInput() } returns "   "

        // When
        editTaskUI.show()

        // Then
        verify { viewer.printError("Invalid task number.") }
        verify(exactly = 0) { getProjectByIdUseCase.getProjectById(any()) }
        verify(exactly = 0) { editTaskUseCase.editTask(any(), any(), any(), any()) }
    }
    @Test
    fun `should show error when database is unavailable while retrieving tasks`() {
        // Given
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns
                Result.failure(Exception("Database not available"))

        // When
        editTaskUI.show()

        // Then
        verify { viewer.printError("Failed to retrieve tasks: Database not available") }
        verify(exactly = 0) { getProjectByIdUseCase.getProjectById(any()) }
        verify(exactly = 0) { editTaskUseCase.editTask(any(), any(), any(), any()) }
    }

    @Test
    fun `should update task with valid input`() {
        // Given
        val tasks = listOf(task)
        val newTitle = "Updated Task Title"
        val newDescription = "Updated Task Description"
        val selectedState = inProgressState

        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { getProjectByIdUseCase.getProjectById(projectId) } returns Result.success(project)
        every { reader.readInput() } returnsMany listOf(
            "1",
            newTitle,
            newDescription,
            "2"
        )
        every { editTaskUseCase.editTask(task, newTitle, newDescription, selectedState) } just runs

        // When
        editTaskUI.show()

        // Then
        verify { editTaskUseCase.editTask(task, newTitle, newDescription, selectedState) }
        verify { viewer.printInfoLine("Task updated successfully!") }
    }

    @Test
    fun `should update task state when new state is selected`() {
        // Given
        val tasks = listOf(task)
        val newTitle = "Updated Task Title"
        val newDescription = "Updated Task Description"
        val selectedState = inProgressState

        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { getProjectByIdUseCase.getProjectById(projectId) } returns Result.success(project)
        every { reader.readInput() } returnsMany listOf(
            "1",
            newTitle,
            newDescription,
            "2"
        )
        every { editTaskUseCase.editTask(task, newTitle, newDescription, selectedState) } just runs

        // When
        editTaskUI.show()

        // Then
        verify { editTaskUseCase.editTask(task, newTitle, newDescription, selectedState) }
        verify { viewer.printInfoLine("Task updated successfully!") }
    }

    @Test
    fun `should retain the current state when invalid state input is provided`() {
        // Given
        val tasks = listOf(task)
        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { getProjectByIdUseCase.getProjectById(projectId) } returns Result.success(project)
        every { reader.readInput() } returnsMany listOf(
            "1",
            "",
            "",
            "invalid"
        )

        every { editTaskUseCase.editTask(task, task.title, task.description, task.state) } just runs

        // When
        editTaskUI.show()

        // Then
        verify { viewer.printInfoLine("Keeping the current state: To Do") }
        verify { editTaskUseCase.editTask(task, task.title, task.description, task.state) }
        verify { viewer.printInfoLine("Task updated successfully!") }
    }
    @Test
    fun `should update task state when valid state input is provided`() {
        // Given
        val tasks = listOf(task)
        val newTitle = "Updated Task Title"
        val newDescription = "Updated Task Description"
        val selectedState = inProgressState

        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { getProjectByIdUseCase.getProjectById(projectId) } returns Result.success(project)
        every { reader.readInput() } returnsMany listOf(
            "1",
            newTitle,
            newDescription,
            "2"
        )
        every { editTaskUseCase.editTask(task, newTitle, newDescription, selectedState) } just runs

        // When
        editTaskUI.show()

        // Then
        verify { editTaskUseCase.editTask(task, newTitle, newDescription, selectedState) }
        verify { viewer.printInfoLine("Task updated successfully!") }
    }
    @Test
    fun `should keep existing title and description when input is empty`() {
        // Given
        val tasks = listOf(task)
        val newTitle = ""
        val newDescription = ""

        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { getProjectByIdUseCase.getProjectById(projectId) } returns Result.success(project)
        every { reader.readInput() } returnsMany listOf(
            "1",
            newTitle,
            newDescription,
            "invalid"
        )

        every { editTaskUseCase.editTask(task, task.title, task.description, task.state) } just runs

        // When
        editTaskUI.show()

        // Then
        verify { editTaskUseCase.editTask(task, task.title, task.description, task.state) }
        verify { viewer.printInfoLine("Task updated successfully!") }
    }


    @Test
    fun `should show error message when task update fails`() {
        // Given
        val tasks = listOf(task)
        val newTitle = "Updated Task Title"
        val newDescription = "Updated Task Description"
        val selectedState = inProgressState

        every { getTasksForProjectUseCase.getTasksForProject(projectId) } returns Result.success(tasks)
        every { getProjectByIdUseCase.getProjectById(projectId) } returns Result.success(project)
        every { reader.readInput() } returnsMany listOf(
            "1",
            newTitle,
            newDescription,
            "2"
        )
        every { editTaskUseCase.editTask(task, newTitle, newDescription, selectedState) } throws Exception("Update failed")

        // When
        editTaskUI.show()

        // Then
        verify { viewer.printError("Failed to update task: Update failed") }
    }

  

}