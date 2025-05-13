package domain.use_case.task

import creator_helper.createStateHelper
import creator_helper.createTaskHelper
import ui.common.exception.EmptyFieldException
import domain.repository.TaskRepository
import domain.use_case.log.CreateTaskLogUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import java.util.*
import kotlin.test.Test
import kotlin.test.assertFailsWith

class EditTaskUseCaseTest {
    private val taskRepository: TaskRepository = mockk(relaxed = true)
    private val createTaskLogUseCase : CreateTaskLogUseCase = mockk(relaxed = true)
    private lateinit var editTaskUseCase: EditTaskUseCase

    @BeforeEach
    fun setUp() {
        clearAllMocks()
        editTaskUseCase = EditTaskUseCase(taskRepository, createTaskLogUseCase)
    }

    @Test
    fun `editTask updates  title and state when description is blank`() = runTest {
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
    fun `editTask updates  description and state when title is blank`() = runTest {
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

        val editorId = UUID.randomUUID()

        coEvery { taskRepository.editTask(any()) } just Runs
        coEvery { createTaskLogUseCase.createTaskLog(any(), any(), any()) } just Runs

        // When
        editTaskUseCase.editTask(task, null, newDescription,  task.state, editorId)

        // Then
        coVerify {
            taskRepository.editTask(match {
                it.title == task.title &&
                        it.description == newDescription &&
                        it.state ==  task.state
            })
            createTaskLogUseCase.createTaskLog(editorId, task, any())
        }
    }

    @Test
    fun `editTask handles null description and title correctly`() = runTest {
        // Given
        val task = createTaskHelper(title = "Original Title", description = "Original Description")
        val newTitle = "New Title"
        val editorId = UUID.randomUUID()

        coEvery { taskRepository.editTask(any()) } just Runs
        coEvery { createTaskLogUseCase.createTaskLog(any(), any(), any()) } just Runs

        // When
        editTaskUseCase.editTask(task, newTitle, null, task.state, editorId)

        // Then
        coVerify {
            taskRepository.editTask(match {
                it.title == newTitle &&
                        it.description == task.description &&
                        it.state == task.state
            })
            createTaskLogUseCase.createTaskLog(editorId, task, any())
        }
    }


    @Test
    fun `editTask does not throw when only state is changed and title and description are blank`() = runTest {
        // Given

        val task = createTaskHelper()
        val newState = createStateHelper(name = "Changed State")

        coEvery { taskRepository.editTask(any()) } just Runs
        coEvery { createTaskLogUseCase.createTaskLog(any(), any(), any()) } just Runs
        // When

        editTaskUseCase.editTask(task, "  ", "  ", newState, UUID.randomUUID())
        // Then

        coVerify {
            taskRepository.editTask(any())
        }
    }

    @Test
    fun `editTask does not throw when title is blank but description is provided and state not changed`() = runTest {
        // Given

        val task = createTaskHelper()
        val editorId = UUID.randomUUID()

        coEvery { taskRepository.editTask(any()) } just Runs
        coEvery { createTaskLogUseCase.createTaskLog(any(), any(), any()) } just Runs
        // When

        editTaskUseCase.editTask(task, "   ", "Updated Description", task.state, editorId)
        // Then

        coVerify {
            taskRepository.editTask(match {
                it.description == "Updated Description" &&
                        it.title == task.title &&
                        it.state == task.state
            })
        }
    }
    @Test
    fun `editTask does not throw when description is blank but title is provided and state not changed`() = runTest {
        // Given

        val task = createTaskHelper()
        val editorId = UUID.randomUUID()

        coEvery { taskRepository.editTask(any()) } just Runs
        coEvery { createTaskLogUseCase.createTaskLog(any(), any(), any()) } just Runs
        // When

        editTaskUseCase.editTask(task, "Updated Title", "   ", task.state, editorId)
        // Then

        coVerify {
            taskRepository.editTask(match {
                it.title == "Updated Title" &&
                        it.description == task.description &&
                        it.state == task.state
            })
        }
    }

    @Test
    fun `editTask throws EmptyFieldException when no changes are made to task`() = runTest {
        // Given
        val task = createTaskHelper()
        val editorId = UUID.randomUUID()

        // When & Then
        assertFailsWith<EmptyFieldException> {
            editTaskUseCase.editTask(
                task = task,
                newTitle = null,
                newDescription = null,
                newState = task.state,
                editorUserId = editorId
            )
        }
    }

    @Test
    fun `editTask throws EmptyFieldException when only whitespace is provided for title and description`() = runTest {
        // Given
        val task = createTaskHelper()
        val editorId = UUID.randomUUID()

        // When & Then
        assertFailsWith<EmptyFieldException> {
            editTaskUseCase.editTask(
                task = task,
                newTitle = "   ",
                newDescription = "   ",
                newState = task.state,
                editorUserId = editorId
            )
        }
    }

}
