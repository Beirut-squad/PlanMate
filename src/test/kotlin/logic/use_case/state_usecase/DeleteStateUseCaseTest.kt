package logic.use_case.state_usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertTrue
import org.example.logic.repositories.state_repository.StateRepository
import org.example.logic.use_cases.state_usecase.DeleteStateUseCase
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
class DeleteStateUseCaseTest {

    private val stateRepository: StateRepository = mockk()
    private lateinit var deleteStateUseCase: DeleteStateUseCase

    @BeforeTest
    fun setup() {
        deleteStateUseCase = DeleteStateUseCase(stateRepository)
    }

    @Test
    fun `should delete state successfully`() {
        // When
        val id = UUID.randomUUID()

        // Given
        every { stateRepository.deleteState(id) } returns Result.success(Unit)

        // Then
        val result = deleteStateUseCase.deleteState(id)

        assertTrue(result.isSuccess)
        verify(exactly = 1) { stateRepository.deleteState(id) }
    }

    @Test
    fun `should return failure when repository throws exception`() {
        // When
        val id = UUID.randomUUID()
        val exception = RuntimeException("Delete failed")

        // Given
        every { stateRepository.deleteState(id) } returns Result.failure(exception)

        // Then
        val result = deleteStateUseCase.deleteState(id)

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        verify(exactly = 1) { stateRepository.deleteState(id) }
    }

}
