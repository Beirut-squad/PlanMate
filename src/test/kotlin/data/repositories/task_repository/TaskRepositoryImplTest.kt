package data.repositories.task_repository

import creator_helper.createTaskHelper
import io.mockk.*

import io.mockk.mockk
import org.example.data.datasource.log_data_source.LogDataSource
import org.example.data.datasource.task_data_source.TaskDataSource
import org.example.data.repositories.task_repository.TaskRepositoryImpl
import org.example.logic.repositories.task_repository.TaskRepository
import org.example.models.EntityType
import org.example.models.Log
import org.example.models.Loggable
import org.example.models.Task
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

class TaskRepositoryImpl {
    private var taskDataSource: TaskDataSource = mockk(relaxed = true)
    private var logDataSource: LogDataSource = mockk(relaxed = true)
    private lateinit var taskRepository: TaskRepository
    @BeforeEach
    fun setup() {
        taskRepository = TaskRepositoryImpl(taskDataSource, logDataSource)
    }
    @Test
    fun `create Task should call createTask in taskDataSource`() {
        // Given
        val task = createTaskHelper()

        // When
        taskRepository.createTask(task)

        // Then
        verify(exactly = 1) { taskDataSource.createTask(task) }
    }




}