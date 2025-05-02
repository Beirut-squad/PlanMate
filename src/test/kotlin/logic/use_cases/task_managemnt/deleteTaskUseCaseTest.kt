package logic.use_cases.task_managemnt

import creator_helper.createTaskHelper
import io.mockk.*
import logic.use_cases.log.CreateTaskLogUseCase
import org.example.logic.exceptions.TaskDeletionException
import org.example.logic.repositories.task_repository.TaskRepository
import org.example.logic.use_cases.task_managemnt.DeleteTaskUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertFailsWith

class deleteTaskUseCaseTest {
    private var taskRepository: TaskRepository = mockk(relaxed = true)
    private var createTaskLogUseCase: CreateTaskLogUseCase =mockk(relaxed = true)
    private lateinit var deleteTaskUseCase : DeleteTaskUseCase
    @BeforeEach
    fun setup() {
        deleteTaskUseCase = DeleteTaskUseCase(taskRepository,createTaskLogUseCase)
    }

    @Test
    fun `deleteTask should call repository and log use case when successful`() {
        // Given
        val task = createTaskHelper()
        val taskId = UUID.randomUUID()
        every { taskRepository.deleteTask(taskId) } returns Result.success(Unit)

        // When
        deleteTaskUseCase.deleteTask(task,taskId)

        // Then
        verify { taskRepository.deleteTask(taskId) }
        verify {  createTaskLogUseCase.createTaskLog(task, null, task.creatorUserID)}

    }
    @Test
    fun `deleteTask should throw exception when deletion fails`() {
        // Given
        val task =createTaskHelper()
        val taskId = UUID.randomUUID()
        val exception = TaskDeletionException("Failed to delete task")
        every { taskRepository.deleteTask(taskId) } returns Result.failure(exception)

        // Then
        assertFailsWith<TaskDeletionException> {
            deleteTaskUseCase.deleteTask(task,taskId)
        }

        verify { taskRepository.deleteTask(taskId) }
        verify(exactly = 0) { createTaskLogUseCase.createTaskLog(task, null, task.creatorUserID) }
    }


}