package data.repositories.task_repository

import creator_helper.createTaskHelper
import io.mockk.*

import io.mockk.mockk
import org.example.data.datasource.log_data_source.LogDataSource
import org.example.data.datasource.task_data_source.TaskDataSource
import org.example.data.repositories.task_repository.TaskRepositoryImpl
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
        every { taskDataSource.createTask(any()) } returns Result.success("Task created successfully")

        // When
        val result = taskRepository.createTask(task)

        // Then
        assertTrue(result.isSuccess)
        assertEquals("Task created successfully", result.getOrNull())
    }


}