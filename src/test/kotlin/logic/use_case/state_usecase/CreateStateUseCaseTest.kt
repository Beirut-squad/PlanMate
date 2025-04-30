package logic.use_case.state_usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertTrue
import org.example.logic.repositories.state_repository.StateRepository
import org.example.logic.use_case.state_usecase.CreateStateUseCase
import org.example.models.State
import org.junit.jupiter.api.BeforeEach
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateStateUseCaseTest {
    private lateinit var stateRepository: StateRepository
    private lateinit var createStateUseCase: CreateStateUseCase


    @BeforeEach
    fun setup() {
        stateRepository = mockk(relaxed = true)
        createStateUseCase = CreateStateUseCase(stateRepository)
    }

    @Test
    fun `should create state successfully when name is valid`() {
        // When
        val id = UUID.randomUUID()
        val stateName = "TODO"
        val state = State(id, stateName)

        // Given
        every { stateRepository.createState(state) } returns Result.success(State(id, stateName))

        // Then
        val result = createStateUseCase.createState(state)
        verify(exactly = 1) { stateRepository.createState(state) }
        assertTrue(result.isSuccess)
        assertEquals(state, result.getOrNull())
    }


    @Test
    fun `should return failure when name is blank`() {
        // When
        val id = UUID.randomUUID()
        val blankName = ""
        val state = State(id, blankName)

        // Given
        val result = createStateUseCase.createState(state)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
        assertEquals("Empty name", result.exceptionOrNull()?.message)
    }

    @Test
    fun `should return failure when repository fails`() {
        // When
        val id = UUID.randomUUID()
        val name = "TODO"
        val state = State(id, name)
        val exception = RuntimeException("Repo failed")

        // Given
        every { stateRepository.createState(any()) } returns Result.failure(exception)
        val result = createStateUseCase.createState(state)

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        verify { stateRepository.createState(any()) }
    }
}
