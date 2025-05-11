package domain.use_case.task

import creator_helper.createTaskHelper
import domain.use_case.log.CreateTaskLogUseCase
import io.mockk.*
import kotlinx.coroutines.test.*
import org.example.domain.repository.TaskRepository
import org.junit.jupiter.api.*
import java.util.UUID

class DeleteTaskUseCaseTest {
    private var taskRepository: TaskRepository = mockk(relaxed = true)
    private var createTaskLogUseCase : CreateTaskLogUseCase = mockk(relaxed = true)
    private lateinit var deleteTaskUseCase: DeleteTaskUseCase

    @BeforeEach
    fun setUp() {
        clearAllMocks()
        deleteTaskUseCase = DeleteTaskUseCase(taskRepository, createTaskLogUseCase)
    }

    @Test
    fun `deleteTask should delete task and create log`() = runTest {
        // Arrange
        val userID = UUID.randomUUID()
        val task = createTaskHelper()

        // Act
        deleteTaskUseCase.deleteTask(task, userID)

        // Assert
        coVerify(exactly = 1) { taskRepository.deleteTask(task.id) }
        coVerify(exactly = 1) { createTaskLogUseCase.createTaskLog(userID, task, null) }
    }



}