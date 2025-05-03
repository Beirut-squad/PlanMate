package logic.use_case.state_usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import junit.framework.TestCase.assertTrue
import org.example.logic.repositories.state_repository.StateRepository
import org.example.logic.use_cases.state_usecase.CreateStateUseCase
import org.example.models.State
import org.example.models.Task
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateStateUseCaseTest {
    private lateinit var stateRepository: StateRepository
    private lateinit var createStateUseCase: CreateStateUseCase
    private val id = UUID.randomUUID()

    @BeforeEach
    fun setup() {
        stateRepository = mockk(relaxed = true)
        createStateUseCase = CreateStateUseCase(stateRepository)
    }

    @Test
    fun `should return failure when name is blank`() {
        // When
        val blankName = ""
        val state = State(id, blankName)

        // Given
        val result = createStateUseCase.createState(blankName)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
        assertEquals("Create failed : name is Blank !!", result.exceptionOrNull()?.message)
    }

    @Test
    fun `should return failure when repository fails`() {
        // When
        val name = "TODO"
        val state = State(id, name)
        val exception = RuntimeException("Repo failed")

        // Given
        every { stateRepository.createState(any()) } returns Result.failure(exception)
        val result = createStateUseCase.createState(name)

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        verify { stateRepository.createState(any()) }
    }
}
