package logic.use_cases.task_managemnt

import creator_helper.createTaskHelper
import io.mockk.*
import logic.use_cases.log.CreateTaskLogUseCase
import org.example.logic.exceptions.NoFieldsToUpdateException
import org.example.logic.exceptions.TaskEditException
import org.example.logic.repositories.task_repository.TaskRepository
import org.example.logic.use_cases.task_managemnt.EditTaskUseCase
import org.example.models.Task
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertFailsWith

class editTaskUseCaseTest {
    private var taskRepository: TaskRepository = mockk(relaxed = true)
    private var createTaskLogUseCase: CreateTaskLogUseCase = mockk(relaxed = true)
    private lateinit var editTaskUseCase: EditTaskUseCase

    @BeforeEach
    fun setup() {
        editTaskUseCase = EditTaskUseCase(taskRepository, createTaskLogUseCase)
    }

    @Test
    fun `editTask should update task when valid fields are provided`() {
        // Given
        val task = createTaskHelper()

        val newTitle = "New Title"
        val newDescription = "New Description"
        val newState = "In Progress"

        every { taskRepository.editTask(any()) } returns Result.success(Unit)

        // When
        editTaskUseCase.editTask(task, newTitle, newDescription, newState)

        // Then
        assertEquals("Test Task Title", task.title)
        assertEquals("Test Task Description", task.description)
        assertEquals("To Do", task.state.name)

        val updatedTaskSlot = slot<Task>()
        verify { taskRepository.editTask(capture(updatedTaskSlot)) }

        val capturedTask = updatedTaskSlot.captured
        assertEquals(newTitle, capturedTask.title)
        assertEquals(newDescription, capturedTask.description)
        assertEquals(newState, capturedTask.state.name)
        assertNotNull(capturedTask.updatedAt)
    }

    @Test
    fun `editTask should update task when at least one field is provided`() {
        // Given
        val task = createTaskHelper()
        val newTitle = "New Title"
        val newDescription = null
        val newState = null

        every { taskRepository.editTask(any()) } returns Result.success(Unit)

        // When
        editTaskUseCase.editTask(task, newTitle, newDescription, newState)

        // Then
        val updatedTaskSlot = slot<Task>()
        verify { taskRepository.editTask(capture(updatedTaskSlot)) }

        val capturedTask = updatedTaskSlot.captured
        assertEquals(newTitle, capturedTask.title)
        assertEquals("Test Task Description", capturedTask.description)
        assertEquals("To Do", capturedTask.state.name)
        assertNotNull(capturedTask.updatedAt)
    }

    @Test
    fun `editTask should update description and state when only they are provided`() {
        // Given
        val task = createTaskHelper()
        val newTitle = null
        val newDescription = "New Description"
        val newState = "In Progress"

        every { taskRepository.editTask(any()) } returns Result.success(Unit)

        // When
        editTaskUseCase.editTask(task, newTitle, newDescription, newState)

        // Then
        val updatedTaskSlot = slot<Task>()
        verify { taskRepository.editTask(capture(updatedTaskSlot)) }

        val capturedTask = updatedTaskSlot.captured
        assertEquals(task.title, capturedTask.title)
        assertEquals(newDescription, capturedTask.description)
        assertEquals(newState, capturedTask.state.name)
        assertNotNull(capturedTask.updatedAt)
        verify { createTaskLogUseCase.createTaskLog(task, capturedTask, task.creatorUserID) }

    }

    @Test
    fun `editTask should throw exception when edit fails`() {
        // Given
        val task = createTaskHelper()
        val newTitle = "test"
        val newDescription = "New Description"
        val newState = "In Progress"
        val exception = TaskEditException("Failed to edit task")
        every { taskRepository.editTask(any()) } returns Result.failure(exception)

        // Then&When
        assertFailsWith<TaskEditException> {
            editTaskUseCase.editTask(task, newTitle, newDescription, newState)
        }

        verify { taskRepository.editTask(any()) }
        verify(exactly = 0) { createTaskLogUseCase.createTaskLog(any(), any(), task.creatorUserID) }
    }

    @Test
    fun `editTask should update description and title when only they are provided`() {
        // Given
        val task = createTaskHelper()
        val newTitle = null
        val newDescription = null
        val newState = "to do"

        every { taskRepository.editTask(any()) } returns Result.success(Unit)

        // When
        editTaskUseCase.editTask(task, newTitle, newDescription, newState)

        // Then
        val updatedTaskSlot = slot<Task>()
        verify { taskRepository.editTask(capture(updatedTaskSlot)) }

        val capturedTask = updatedTaskSlot.captured
        assertEquals(task.title, capturedTask.title)
        assertEquals(task.description, capturedTask.description)
        assertEquals(newState, capturedTask.state.name)
        assertNotNull(capturedTask.updatedAt)
    }

    @Test
    fun `editTask should not edit task if title is blank but other fields are valid`() {
        // Given
        val task = createTaskHelper()

        val newTitle = "   "
        val newDescription = "New Description"
        val newState = "In Progress"
        every { taskRepository.editTask(any()) } returns Result.success(Unit)

        // When
        editTaskUseCase.editTask(task, newTitle, newDescription, newState)

        // Then
        val updatedTaskSlot = slot<Task>()
        verify { taskRepository.editTask(capture(updatedTaskSlot)) }
        val capturedTask = updatedTaskSlot.captured

        assertEquals(task.title, capturedTask.title)
        assertEquals(newDescription, capturedTask.description)
        assertEquals(newState, capturedTask.state.name)
        assertNotNull(task.updatedAt)
    }

    @Test
    fun `editTask should throw BlankFieldsException when all fields are blank`() {
        // Given
        val task = createTaskHelper()
        val newTitle = ""
        val newDescription = ""
        val newState = ""

        // When & Then
        val exception = assertThrows<NoFieldsToUpdateException> {
            editTaskUseCase.editTask(task, newTitle, newDescription, newState)
        }

        assertEquals("At least one non-blank field must be provided", exception.message)
    }

    @Test
    fun `editTask should throw BlankFieldsException when all fields are null`() {
        // Given
        val task = createTaskHelper()
        val newTitle = null
        val newDescription = null
        val newState = null

        // When & Then
        val exception = assertThrows<NoFieldsToUpdateException> {
            editTaskUseCase.editTask(task, newTitle, newDescription, newState)
        }

        assertEquals("At least one non-blank field must be provided", exception.message)
    }


    @Test
    fun ` should edit  when description is blank and other fields are not`() {
        // Given
        val task = createTaskHelper()
        val newTitle = "test"
        val newDescription = " "
        val newState = "to do"

        every { taskRepository.editTask(any()) } returns Result.success(Unit)

        // When
        editTaskUseCase.editTask(task, newTitle, newDescription, newState)

        // Then
        val updatedTaskSlot = slot<Task>()
        verify { taskRepository.editTask(capture(updatedTaskSlot)) }

        val capturedTask = updatedTaskSlot.captured
        assertEquals(newTitle, capturedTask.title)
        assertEquals(task.description, capturedTask.description)
        assertEquals(newState, capturedTask.state.name)
        assertNotNull(capturedTask.updatedAt)
    }


    @Test
    fun ` should edit  when State is blank and other fields are not`() {
        // Given
        val task = createTaskHelper()
        val newTitle = "test"
        val newDescription = "desc"
        val newState = " "

        every { taskRepository.editTask(any()) } returns Result.success(Unit)

        // When
        editTaskUseCase.editTask(task, newTitle, newDescription, newState)

        // Then
        val updatedTaskSlot = slot<Task>()
        verify { taskRepository.editTask(capture(updatedTaskSlot)) }

        val capturedTask = updatedTaskSlot.captured
        assertEquals(newTitle, capturedTask.title)
        assertEquals(newDescription, capturedTask.description)
        assertEquals(task.state.name, capturedTask.state.name)
        assertNotNull(capturedTask.updatedAt)
    }
}