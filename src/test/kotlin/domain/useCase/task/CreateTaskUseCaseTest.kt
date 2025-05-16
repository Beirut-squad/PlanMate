package domain.useCase.task

import creator_helper.createStateHelper
import creator_helper.createTaskHelper
import ui.common.exception.EmptyTaskDescriptionException
import ui.common.exception.EmptyTaskTitleException
import domain.model.Task
import domain.repository.TaskRepository
import domain.useCase.log.CreateTaskLogUseCase
import io.mockk.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class CreateTaskUseCaseTest {
    private val taskRepository: TaskRepository = mockk(relaxed = true)
    private val createTaskLogUseCase: CreateTaskLogUseCase = mockk(relaxed = true)
    private lateinit var createTaskUseCase: CreateTaskUseCase

    @BeforeEach
    fun setup() {
        clearAllMocks()
        createTaskUseCase = CreateTaskUseCase(taskRepository, createTaskLogUseCase)
    }

    @Test
    fun `createTask should call taskRepository createTask`() = runTest {
        val task = createTaskHelper()
        val state = task.taskState

        coEvery { taskRepository.createTask(any()) } just Runs
        coEvery { createTaskLogUseCase.createTaskLog(any(), any(), any()) } just Runs

        createTaskUseCase.createTask(task.title, task.description, state, task.projectId, task.creatorUserID)

        coVerify(exactly = 1) { taskRepository.createTask(any()) }
    }

    @Test
    fun `createTask should create a task and log the action`() = runTest {
        // Given
        val title = "Test Task"
        val description = "This is a test task"
        val state = createStateHelper(name = "To Do")
        val projectId = UUID.randomUUID()
        val creatorUserId = UUID.randomUUID()

        coEvery { taskRepository.createTask(any()) } just Runs
        coEvery { createTaskLogUseCase.createTaskLog(any(), any(), any()) } just Runs

        // When
        createTaskUseCase.createTask(title, description, state, projectId, creatorUserId)

        // Then
        coVerify {
            taskRepository.createTask(match { task ->
                task.title == title &&
                        task.description == description &&
                        task.taskState == state &&
                        task.projectId == projectId &&
                        task.creatorUserID == creatorUserId
            })
        }

        coVerify { createTaskLogUseCase.createTaskLog(eq(creatorUserId), null, any()) }
    }

    @Test
    fun `createTask should throw exception when title is blank`() = runTest {
        // Given
        val title = "   "
        val description = "Test description"
        val state = createStateHelper()
        val projectId = UUID.randomUUID()
        val creatorUserId = UUID.randomUUID()

        // When & Then
        assertThrows<EmptyTaskTitleException> {
            runBlocking {
                createTaskUseCase.createTask(title, description, state, projectId, creatorUserId)
            }
        }

        coVerify(exactly = 0) { taskRepository.createTask(any()) }
        coVerify(exactly = 0) { createTaskLogUseCase.createTaskLog(any(), any(), any()) }
    }

    @Test
    fun `createTask should throw exception when description is blank`() = runTest {
        // Given
        val title = "Test title"
        val description = " "
        val state = createStateHelper()
        val projectId = UUID.randomUUID()
        val creatorUserId = UUID.randomUUID()

        // When & Then
        assertThrows<EmptyTaskDescriptionException> {
            runBlocking {
                createTaskUseCase.createTask(title, description, state, projectId, creatorUserId)
            }
        }

        coVerify(exactly = 0) { taskRepository.createTask(any()) }
        coVerify(exactly = 0) { createTaskLogUseCase.createTaskLog(any(), any(), any()) }
    }

    @Test
    fun `createTask should handle different state types`() = runBlocking {
        // Testing with different state names
        val stateNames = listOf("To Do", "In Progress", "Done", "Blocked")

        for (stateName in stateNames) {
            // Given
            val title = "Test Task"
            val description = "This is a test task"
            val state = createStateHelper(name = stateName)
            val projectId = UUID.randomUUID()
            val creatorUserId = UUID.randomUUID()

            clearMocks(taskRepository, createTaskLogUseCase)
            coEvery { taskRepository.createTask(any()) } just Runs
            coEvery { createTaskLogUseCase.createTaskLog(any(), any(), any()) } just Runs

            // When
            createTaskUseCase.createTask(title, description, state, projectId, creatorUserId)

            // Then
            coVerify {
                taskRepository.createTask(match { task ->
                    task.taskState.name == stateName
                })
            }
        }
    }

    @Test
    fun `createTask should use provided project ID and creator user ID`() = runTest {
        // Given
        val title = "Test Task"
        val description = "This is a test task"
        val state = createStateHelper()
        val projectId = UUID.randomUUID()
        val creatorUserId = UUID.randomUUID()

        val capturedTask = slot<Task>()
        coEvery { taskRepository.createTask(capture(capturedTask)) } just Runs

        // When
        createTaskUseCase.createTask(title, description, state, projectId, creatorUserId)

        // Then
        assertEquals(projectId, capturedTask.captured.projectId)
        assertEquals(creatorUserId, capturedTask.captured.creatorUserID)
    }

    @Test
    fun `createTask should set created and updated timestamps`() = runTest {
        // Given
        val title = "Test Task"
        val description = "This is a test task"
        val state = createStateHelper()
        val projectId = UUID.randomUUID()
        val creatorUserId = UUID.randomUUID()

        val capturedTask = slot<Task>()
        coEvery { taskRepository.createTask(capture(capturedTask)) } just Runs

        // When
        createTaskUseCase.createTask(title, description, state, projectId, creatorUserId)

        // Then
        assertTrue(capturedTask.isCaptured)
        assertNotNull(capturedTask.captured.createdAt)
        assertNotNull(capturedTask.captured.updatedAt)
    }


}