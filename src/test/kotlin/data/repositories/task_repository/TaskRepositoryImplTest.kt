package data.repositories.task_repository

import creator_helper.createTaskHelper
import io.mockk.*
import org.example.data.datasource.log_data_source.LogDataSource
import org.example.data.datasource.task_data_source.TaskDataSource
import org.example.data.repositories.task_repository.TaskRepositoryImpl
import org.example.logic.exceptions.TaskCreationException
import org.example.logic.exceptions.TaskDeletionException
import org.example.logic.exceptions.TaskEditException
import org.example.logic.repositories.task_repository.TaskRepository

import org.example.models.Task
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.*


class TaskRepositoryImpl {
    private var taskDataSource: TaskDataSource = mockk(relaxed = true)
    private lateinit var taskRepository: TaskRepository


    @BeforeEach
    fun setup() {
        taskRepository = TaskRepositoryImpl(taskDataSource)
    }

    @Test
    fun `createTask should return success when data source returns success`() {
        // Given
        val task = createTaskHelper()
        every { taskDataSource.createTask(any()) } returns Result.success(Unit)

        // When
        val result = taskRepository.createTask(task)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(Unit, result.getOrNull())
    }

    @Test
    fun `createTask should return failure when data source returns failure`() {
        // Given
        val task = createTaskHelper()
        every { taskDataSource.createTask(any()) } returns Result.failure(TaskCreationException("Task creation failed"))

        // When
        val result = taskRepository.createTask(task)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is TaskCreationException)
    }





    @Test
    fun `editTask should return success when data source returns success`() {
        // Given
        val task = createTaskHelper()
        every { taskDataSource.editTask(any()) } returns Result.success(Unit)

        // When
        val result = taskRepository.editTask(task)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(Unit, result.getOrNull())
    }

    @Test
    fun `editTask should return failure when data source returns failure`() {
        // Given
        val task = createTaskHelper()
        every { taskDataSource.editTask(any()) } returns Result.failure(TaskEditException("Failed to edit task"))

        // When
        val result = taskRepository.editTask(task)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is TaskEditException)
    }
    @Test
    fun `deleteTask should return success when data source returns success`() {
        // Given
        val taskId = UUID.randomUUID()
        every { taskDataSource.deleteTask(taskId) } returns Result.success(Unit)

        // When
        val result = taskRepository.deleteTask(taskId)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(Unit, result.getOrNull())
    }
    @Test
    fun `deleteTask should return failure when data source returns failure`() {
        // Given
        val taskId = UUID.randomUUID()
        val exception = TaskDeletionException("Failed to delete task")
        every { taskDataSource.deleteTask(taskId) } returns Result.failure(exception)

        // When
        val result = taskRepository.deleteTask(taskId)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is TaskDeletionException)
        assertEquals("Failed to delete task", result.exceptionOrNull()?.message)
    }
}