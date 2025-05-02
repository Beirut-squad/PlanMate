package data.repositories.state_repository

import data.datasource.state_data_source.StateDataSource
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.data.repositories.state_repository.StateRepositoryImpl
import org.example.models.State
import org.junit.jupiter.api.BeforeEach
import java.util.UUID
import kotlin.test.*

class StateRepositoryImplTest {
    private lateinit var stateDataSource: StateDataSource
    private lateinit var stateRepository: StateRepositoryImpl

    @BeforeEach
    fun setUp() {
        stateDataSource = mockk(relaxed = true)
        stateRepository = StateRepositoryImpl(stateDataSource)
    }

    @Test
    fun `should create state successfully`() {
        // When
        val state = State(id = UUID.randomUUID(), name = "TODO")

        // Given
        stateRepository.createState(state)

        // Then
        verify(exactly = 1) { stateDataSource.createState(state) }
    }


    @Test
    fun `should edit state successfully`() {
        // When
        val state = State(UUID.randomUUID(), "IN_PROGRESS")

        // Given
        stateRepository.editState(state)

        // Then
        verify(exactly = 1) { stateDataSource.editState(state) }
    }


    @Test
    fun `should delete state successfully`() {
        // When
        val id = UUID.randomUUID()

        // Given
        stateRepository.deleteState(id)

        // Then
        verify(exactly = 1) { stateDataSource.deleteState(id) }
    }

}
