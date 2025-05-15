package domain.useCase.task

import creator_helper.createTaskHelper
import domain.repository.TaskRepository
import domain.useCase.log.CreateTaskLogUseCase
import io.mockk.*
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.*
import java.util.UUID

class DeleteTaskUseCaseTest {
    private val taskRepository: TaskRepository = mockk(relaxed = true)
    private val createTaskLogUseCase : CreateTaskLogUseCase = mockk(relaxed = true)
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