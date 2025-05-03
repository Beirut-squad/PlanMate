package logic.use_case.state_usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertTrue
import logic.repositories.task_state_repository.TaskStateRepository
import org.example.logic.use_case.task_state_usecase.EditTaskStateUseCase
import org.example.models.TaskState
import org.junit.jupiter.api.BeforeEach
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class EditTaskStateUseCaseTest {
    private lateinit var taskStateRepository: TaskStateRepository
    private lateinit var editTaskStateUseCase: EditTaskStateUseCase

    @BeforeEach
    fun setup() {
        taskStateRepository = mockk(relaxed = true)
        editTaskStateUseCase = EditTaskStateUseCase(taskStateRepository)
    }

    @Test
    fun `should edit state successfully when new name is valid`() {
        // When
        val id = UUID.randomUUID()
        val oldTaskState = TaskState(id, "Old Name")
        val newName = "New Name"
        val updatedTaskState = TaskState(id, newName)

        // Given
        every { taskStateRepository.editTaskState(updatedTaskState) } returns Result.success(updatedTaskState)

        // Then
        val result = editTaskStateUseCase.editTaskState(oldTaskState, newName)

        verify(exactly = 1) { taskStateRepository.editTaskState(updatedTaskState) }
        assertTrue(result.isSuccess)
        assertEquals(updatedTaskState, result.getOrNull())
    }
    @Test
    fun `should return failure when new state name is blank`() {
        // When
        val id = UUID.randomUUID()
        val oldTaskState = TaskState(id, "old name")
        val newName = ""

        // Given
        val result = editTaskStateUseCase.editTaskState(oldTaskState, newName)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
        assertEquals("Edit failed : name is Blank !!", result.exceptionOrNull()?.message)
    }

    @Test
    fun `should return failure when repository fails`() {
        // When
        val id = UUID.randomUUID()
        val oldTaskState = TaskState(id, "Old Name")
        val newName = "Updated Name"
        val updatedTaskState = TaskState(id, newName)
        val exception = RuntimeException("Repo failed")

        // Given
        every { taskStateRepository.editTaskState(updatedTaskState) } returns Result.failure(exception)

        val result = editTaskStateUseCase.editTaskState(oldTaskState, newName)

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        verify { taskStateRepository.editTaskState(updatedTaskState) }
    }


}