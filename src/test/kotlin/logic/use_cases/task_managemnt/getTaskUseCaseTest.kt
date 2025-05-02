package logic.use_cases.task_managemnt

import creator_helper.createTaskHelper
import io.mockk.*
import logic.use_cases.log.CreateTaskLogUseCase
import org.example.logic.exceptions.GetTaskException
import org.example.logic.repositories.task_repository.TaskRepository
import org.example.logic.use_cases.log.GetTaskLogsByTaskIdUseCase
import org.example.logic.use_cases.task_managemnt.GetTaskUseCase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class getTaskUseCaseTest {
    private var taskRepository: TaskRepository = mockk(relaxed = true)
    private var getTaskLogsByTaskIdUseCase: GetTaskLogsByTaskIdUseCase = mockk(relaxed = true)
    private lateinit var getTaskUseCase: GetTaskUseCase

    @BeforeEach
    fun setup() {
        getTaskUseCase = GetTaskUseCase(taskRepository, getTaskLogsByTaskIdUseCase)
    }

    @Test
    fun `getTask should return task when repository succeeds`() {
        // Given
        val task = createTaskHelper()
        val taskId = UUID.randomUUID()
        every { taskRepository.getTask(taskId) } returns Result.success(task)

        // When
        val result = getTaskUseCase.getTask(taskId)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(task, result.getOrNull())
        verify {
            taskRepository.getTask(taskId)
            getTaskLogsByTaskIdUseCase.getTaskLogsByTaskId(taskId)
        }
    }

    @Test
    fun `getTask should throw exception when repository fails`() {
        // Given
        val taskId = UUID.randomUUID()
        val exception = GetTaskException("Failed to retrieve task")
        every { taskRepository.getTask(taskId) } returns Result.failure(exception)

        // Then
        val thrown = assertThrows<GetTaskException> {
            getTaskUseCase.getTask(taskId)
        }
        assertEquals("Failed to retrieve task", thrown.message)
        verify { taskRepository.getTask(taskId) }
        verify(exactly = 0) { getTaskLogsByTaskIdUseCase.getTaskLogsByTaskId(taskId) }

    }
}