package logic.use_case.state_usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertTrue
import logic.repositories.task_state_repository.TaskStateRepository
import org.example.logic.use_case.task_state_usecase.DeleteTaskStateUseCase
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
class DeleteTaskStateUseCaseTest {

    private val taskStateRepository: TaskStateRepository = mockk()
    private lateinit var deleteTaskStateUseCase: DeleteTaskStateUseCase

    @BeforeTest
    fun setup() {
        deleteTaskStateUseCase = DeleteTaskStateUseCase(taskStateRepository)
    }

    @Test
    fun `should delete state successfully`() {
        // When
        val id = UUID.randomUUID()

        // Given
        every { taskStateRepository.deleteTaskState(id) } returns Result.success(Unit)

        // Then
        val result = deleteTaskStateUseCase.deleteTaskState(id)

        assertTrue(result.isSuccess)
        verify(exactly = 1) { taskStateRepository.deleteTaskState(id) }
    }

    @Test
    fun `should return failure when repository throws exception`() {
        // When
        val id = UUID.randomUUID()
        val exception = RuntimeException("Delete failed")

        // Given
        every { taskStateRepository.deleteTaskState(id) } returns Result.failure(exception)

        // Then
        val result = deleteTaskStateUseCase.deleteTaskState(id)

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        verify(exactly = 1) { taskStateRepository.deleteTaskState(id) }
    }

}
