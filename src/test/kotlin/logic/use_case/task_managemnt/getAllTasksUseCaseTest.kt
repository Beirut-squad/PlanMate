package logic.use_case.task_managemnt

import creator_helper.createTaskHelper
import io.mockk.*
import logic.exceptions.task_management_exception.GetAllTasksException
import org.example.logic.repositories.task_repository.TaskRepository
import org.example.logic.use_cases.log.GetUserTaskLogsUseCase
import org.example.logic.use_cases.task_managemnt.GetAllTasksUseCase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class getAllTasksUseCaseTest {
    private var taskRepository: TaskRepository = mockk(relaxed = true)
    private var getUserTaskLogsUseCase: GetUserTaskLogsUseCase = mockk(relaxed = true)
    private lateinit var getAllTasksUseCase: GetAllTasksUseCase
    private val userId = UUID.randomUUID()
    @BeforeEach
    fun setup() {
        getAllTasksUseCase = GetAllTasksUseCase(taskRepository,getUserTaskLogsUseCase)
    }

    @Test
    fun `getAllTask should return tasks when repository is successful`() {
        // Given
        val tasks = listOf(createTaskHelper(), createTaskHelper())
        every { taskRepository.getAllTasks() } returns Result.success(tasks)
        every { getUserTaskLogsUseCase.getUserTaskLogs(userId) } returns Result.success(emptyList())

        // When
        val result = getAllTasksUseCase.getAllTask(userId)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
        verify {
            taskRepository.getAllTasks()
            getUserTaskLogsUseCase.getUserTaskLogs(userId)
        }
    }

    @Test
    fun `getAllTask should throw exception when repository fails`() {
        // Given
        val exception = GetAllTasksException("Failed to retrieve tasks")
        every { taskRepository.getAllTasks() } returns Result.failure(exception)

        // Then
        val thrown = assertThrows<GetAllTasksException> {
            getAllTasksUseCase.getAllTask(userId)
        }
        assertEquals("Failed to retrieve tasks", thrown.message)
        verify { taskRepository.getAllTasks() }
    }
}