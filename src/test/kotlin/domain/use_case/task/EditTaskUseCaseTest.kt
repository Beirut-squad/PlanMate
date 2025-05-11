package domain.use_case.task

import creator_helper.*
import domain.use_case.log.CreateTaskLogUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.example.domain.exceptions.task_management_exception.NoFieldsToUpdateException
import org.example.domain.repository.TaskRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.*
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertFailsWith

class EditTaskUseCaseTest {
    private var taskRepository: TaskRepository = mockk(relaxed = true)
    private var createTaskLogUseCase : CreateTaskLogUseCase = mockk(relaxed = true)
    private lateinit var editTaskUseCase: EditTaskUseCase

    @BeforeEach
    fun setUp() {
        clearAllMocks()
        editTaskUseCase = EditTaskUseCase(taskRepository, createTaskLogUseCase)
    }

    @Test
    fun `editTask throws exception when both title and description are blank`() = runTest {
        val task = createTaskHelper()
        val state = createStateHelper()

        val exception = assertFailsWith<NoFieldsToUpdateException> {
            editTaskUseCase.editTask(task, "", "   ", state, UUID.randomUUID())
        }

        assertEquals("At least one non-blank field must be provided", exception.message)
    }
    @Test
    fun `editTask updates only title when description is blank`() = runTest {
        val task = createTaskHelper()
        val newTitle = "Updated Title"
        val newState = createStateHelper()

        coEvery { taskRepository.editTask(any()) } just Runs
        coEvery { createTaskLogUseCase.createTaskLog(any(), any(), any()) } just Runs

        editTaskUseCase.editTask(task, newTitle, "   ", newState, UUID.randomUUID())

        coVerify {
            taskRepository.editTask(match {
                it.title == newTitle && it.description == task.description && it.state == newState
            })
        }
    }

    @Test
    fun `editTask updates only description when title is blank`() = runTest {
        val task = createTaskHelper()
        val newDescription = "Updated Description"
        val newState = createStateHelper()

        coEvery { taskRepository.editTask(any()) } just Runs
        coEvery { createTaskLogUseCase.createTaskLog(any(), any(), any()) } just Runs

        editTaskUseCase.editTask(task, "", newDescription, newState, UUID.randomUUID())

        coVerify {
            taskRepository.editTask(match {
                it.description == newDescription && it.title == task.title
            })
        }
    }
    @Test
    fun `editTask updates title and description and logs the change`() = runTest {
        val task = createTaskHelper()
        val newTitle = "New Title"
        val newDescription = "New Desc"
        val newState = createStateHelper()
        val editorId = UUID.randomUUID()

        coEvery { taskRepository.editTask(any()) } just Runs
        coEvery { createTaskLogUseCase.createTaskLog(any(), any(), any()) } just Runs

        editTaskUseCase.editTask(task, newTitle, newDescription, newState, editorId)

        coVerify {
            taskRepository.editTask(match {
                it.title == newTitle && it.description == newDescription && it.state == newState
            })
            createTaskLogUseCase.createTaskLog(editorId, task, any())
        }
    }
    @Test
    fun `editTask handles null title and description correctly`() = runTest {
        // Given
        val task = createTaskHelper(title = "Original Title", description = "Original Description")
        val newDescription = "New Description"
        val newState = createStateHelper(name = "New State")
        val editorId = UUID.randomUUID()

        coEvery { taskRepository.editTask(any()) } just Runs
        coEvery { createTaskLogUseCase.createTaskLog(any(), any(), any()) } just Runs

        // When
        editTaskUseCase.editTask(task, null, newDescription, newState, editorId)

        // Then
        coVerify {
            taskRepository.editTask(match {
                it.title == task.title &&
                        it.description == newDescription &&
                        it.state == newState
            })
            createTaskLogUseCase.createTaskLog(editorId, task, any())
        }
    }

    @Test
    fun `editTask handles null description and title correctly`() = runTest {
        // Given
        val task = createTaskHelper(title = "Original Title", description = "Original Description")
        val newTitle = "New Title"
        val newState = createStateHelper(name = "New State")
        val editorId = UUID.randomUUID()

        coEvery { taskRepository.editTask(any()) } just Runs
        coEvery { createTaskLogUseCase.createTaskLog(any(), any(), any()) } just Runs

        // When
        editTaskUseCase.editTask(task, newTitle, null, newState, editorId)

        // Then
        coVerify {
            taskRepository.editTask(match {
                it.title == newTitle &&
                        it.description == task.description &&
                        it.state == newState
            })
            createTaskLogUseCase.createTaskLog(editorId, task, any())
        }
    }




}
