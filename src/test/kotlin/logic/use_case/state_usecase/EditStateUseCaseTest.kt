package logic.use_case.state_usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertTrue
import org.example.logic.repositories.state_repository.StateRepository
import org.example.logic.use_cases.state_usecase.EditStateUseCase
import org.example.models.State
import org.junit.jupiter.api.BeforeEach
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class EditStateUseCaseTest {
    private lateinit var stateRepository: StateRepository
    private lateinit var editStateUseCase: EditStateUseCase

    @BeforeEach
    fun setup() {
        stateRepository = mockk(relaxed = true)
        editStateUseCase = EditStateUseCase(stateRepository)
    }

    @Test
    fun `should edit state successfully when new name is valid`() {
        // When
        val id = UUID.randomUUID()
        val oldState = State(id, "Old Name")
        val newName = "New Name"
        val updatedState = State(id, newName)

        // Given
        every { stateRepository.editState(updatedState) } returns Result.success(updatedState)

        // Then
        val result = editStateUseCase.editState(oldState, newName)

        verify(exactly = 1) { stateRepository.editState(updatedState) }
        assertTrue(result.isSuccess)
        assertEquals(updatedState, result.getOrNull())
    }
    @Test
    fun `should return failure when new state name is blank`() {
        // When
        val id = UUID.randomUUID()
        val oldState = State(id, "old name")
        val newName = ""

        // Given
        val result = editStateUseCase.editState(oldState, newName)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
        assertEquals("Edit failed : name is Blank !!", result.exceptionOrNull()?.message)
    }

    @Test
    fun `should return failure when repository fails`() {
        // When
        val id = UUID.randomUUID()
        val oldState = State(id, "Old Name")
        val newName = "Updated Name"
        val updatedState = State(id, newName)
        val exception = RuntimeException("Repo failed")

        // Given
        every { stateRepository.editState(updatedState) } returns Result.failure(exception)

        val result = editStateUseCase.editState(oldState, newName)

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        verify { stateRepository.editState(updatedState) }
    }


}