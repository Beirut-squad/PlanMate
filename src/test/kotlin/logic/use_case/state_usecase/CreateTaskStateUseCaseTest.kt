package logic.use_case.state_usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertTrue
import logic.repositories.task_state_repository.TaskStateRepository
import org.example.logic.use_case.task_state_usecase.CreateTaskStateUseCase
import org.example.models.TaskState
import org.junit.jupiter.api.BeforeEach
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateTaskStateUseCaseTest {
    private lateinit var taskStateRepository: TaskStateRepository
    private lateinit var createTaskStateUseCase: CreateTaskStateUseCase
    private val id = UUID.randomUUID()

    @BeforeEach
    fun setup() {
        taskStateRepository = mockk(relaxed = true)
        createTaskStateUseCase = CreateTaskStateUseCase(taskStateRepository)
    }

    @Test
    fun `should return failure when name is blank`() {
        // When
        val blankName = ""
        val taskState = TaskState(id, blankName)

        // Given
        val result = createTaskStateUseCase.createTaskState(blankName)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
        assertEquals("Create failed : name is Blank !!", result.exceptionOrNull()?.message)
    }

    @Test
    fun `should return failure when repository fails`() {
        // When
        val name = "TODO"
        val taskState = TaskState(id, name)
        val exception = RuntimeException("Repo failed")

        // Given
        every { taskStateRepository.createTaskState(any()) } returns Result.failure(exception)
        val result = createTaskStateUseCase.createTaskState(name)

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        verify { taskStateRepository.createTaskState(any()) }
    }
}
