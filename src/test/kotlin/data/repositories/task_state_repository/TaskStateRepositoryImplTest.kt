package data.repositories.task_state_repository

import data.datasource.task_state_data_source.TaskStateDataSource
import io.mockk.mockk
import io.mockk.verify
import org.example.data.repositories.task_state_repository.TaskStateRepositoryImpl
import org.example.models.TaskState
import org.junit.jupiter.api.BeforeEach
import java.util.UUID
import kotlin.test.*

class TaskStateRepositoryImplTest {
    private lateinit var taskStateDataSource: TaskStateDataSource
    private lateinit var taskStateRepository: TaskStateRepositoryImpl

    @BeforeEach
    fun setUp() {
        taskStateDataSource = mockk(relaxed = true)
        taskStateRepository = TaskStateRepositoryImpl(taskStateDataSource)
    }

    @Test
    fun `should create state successfully`() {
        // When
        val taskState = TaskState(id = UUID.randomUUID(), name = "TODO")

        // Given
        taskStateRepository.createTaskState(taskState)

        // Then
        verify(exactly = 1) { taskStateDataSource.createTaskState(taskState) }
    }


    @Test
    fun `should edit state successfully`() {
        // When
        val taskState = TaskState(UUID.randomUUID(), "IN_PROGRESS")

        // Given
        taskStateRepository.editTaskState(taskState)

        // Then
        verify(exactly = 1) { taskStateDataSource.editTaskState(taskState) }
    }


    @Test
    fun `should delete state successfully`() {
        // When
        val id = UUID.randomUUID()

        // Given
        taskStateRepository.deleteTaskState(id)

        // Then
        verify(exactly = 1) { taskStateDataSource.deleteTaskState(id) }
    }

}
