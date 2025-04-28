package data.repositories.task_repository

import creator_helper.createTaskHelper
import creator_helper.createTestLog
import io.mockk.*
import org.example.data.datasource.log_data_source.LogDataSource
import org.example.data.datasource.task_data_source.TaskDataSource
import org.example.data.repositories.task_repository.TaskRepositoryImpl
import org.example.logic.exceptions.TaskCreationException
import org.example.logic.exceptions.TaskEditException
import org.example.logic.repositories.task_repository.TaskRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.*


class TaskRepositoryImpl {
    private var taskDataSource: TaskDataSource = mockk(relaxed = true)
    private var logDataSource: LogDataSource = mockk(relaxed = true)
    private lateinit var taskRepository: TaskRepository
    @BeforeEach
    fun setup() {
        taskRepository = TaskRepositoryImpl(taskDataSource, logDataSource)
    }

    @Test
    fun `createTask should return success when data source returns success`() {
        // Given
        val task = createTaskHelper()
        val log = createTestLog()
        every { taskDataSource.createTask(any()) } returns Result.success("Task created successfully")

        // When
        val result = taskRepository.createTask(task,log)

        // Then
        assertTrue(result.isSuccess)
        assertEquals("Task created successfully", result.getOrNull())
    }
    @Test
    fun `createTask should return failure when data source returns failure`() {
        // Given
        val task = createTaskHelper()
        val log = createTestLog()
        every { taskDataSource.createTask(any()) } returns Result.failure(TaskCreationException("Task creation failed"))

        // When
        val result = taskRepository.createTask(task,log)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is TaskCreationException)
    }

    @Test
    fun `createTask should create a log entry after saving the task when success`() {
        // Given
        val task = createTaskHelper()
        val log = createTestLog()
        every { taskDataSource.createTask(any()) } returns Result.success("Task created successfully")
        every { logDataSource.createLog(any()) } just Runs

        // When
        taskRepository.createTask(task, log)

        // Then
        verify(exactly = 1) { logDataSource.createLog(log) }
    }

    @Test
    fun `createTask should not create a log entry when task creation fails`() {
        // Given
        val task = createTaskHelper()
        val log = createTestLog()
        every { taskDataSource.createTask(any()) } returns Result.failure(TaskCreationException("Task creation failed"))

        // When
         taskRepository.createTask(task, log)

        // Then
        verify(exactly = 0) { logDataSource.createLog(log) }
    }
    @Test
    fun `editTask should return success when data source returns success`() {
        // Given
        val task = createTaskHelper()
        val log = createTestLog()
        every { taskDataSource.editTask(any()) } returns Result.success("Task edited successfully")

        // When
        val result = taskRepository.editTask(task, log)

        // Then
        assertTrue(result.isSuccess)
        assertEquals("Task edited successfully", result.getOrNull())
    }
    @Test
    fun `editTask should return failure when data source returns failure`() {
        // Given
        val task = createTaskHelper()
        val log = createTestLog()
        every { taskDataSource.editTask(any()) } returns Result.failure(TaskEditException("Failed to edit task"))

        // When
        val result = taskRepository.editTask(task, log)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is TaskEditException)
    }


}