package data.datasource.state_data_source

import CsvParser
import io.mockk.*
import org.example.data.csv.CsvReader
import org.example.data.csv.CsvWriter
import org.example.data.datasource.state_data_source.StateDataSourceImpl
import org.example.models.State
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StateDataSourceImplTest {

    private lateinit var writer: CsvWriter<State>
    private lateinit var reader: CsvReader<State>
    private lateinit var dataSource: StateDataSourceImpl
    private lateinit var filePath: String

    @BeforeEach
    fun setUp() {
        writer = mockk(relaxed = true)
        reader = mockk()
        filePath = "test_states.csv"
        dataSource = StateDataSourceImpl(writer, reader, filePath)

        File(filePath).delete()
    }
// region add state tests
    @Test
    fun `should create state and write it to file when file is empty`() {
        // When
        val state = State(UUID.randomUUID(), "TODO")
        val states = listOf<State>()

        // Given
        every { reader.read(any()) } returns states
        every { writer.writeToFile(any(), filePath) } just Runs

        // Then
        val result = dataSource.createState(state)

        verify(exactly = 1) { reader.read(any()) }
        verify(exactly = 1) { writer.writeToFile(states + state, filePath) }

        assertTrue(result.isSuccess)
        assertEquals(state, result.getOrNull())
    }

    @Test
    fun `should read from existing file and append new state`() {
        val state = State(UUID.randomUUID(), "TODO")
        val states = State(UUID.randomUUID(), "Existing")

        File(filePath).writeText("text")

        every { reader.read(any()) } returns listOf(states)
        every { writer.writeToFile(listOf(states, state), filePath) } just Runs

        val result = dataSource.createState(state)

        verify(exactly = 1) { reader.read(any()) }
        verify(exactly = 1) { writer.writeToFile(listOf(states, state), filePath) }

        assertTrue(result.isSuccess)
        assertEquals(state, result.getOrNull())
    }

    @Test
    fun `should return failure when parsing file throws exception`() {
        // When
        val state = State(UUID.randomUUID(), "TODO")
        val exception = RuntimeException("Parsing failed")

        // Given
        every { reader.read(any()) } throws exception

        // Then
        val result = dataSource.createState(state)

        verify(exactly = 1) { reader.read(any()) }
        verify(exactly = 0) { writer.writeToFile(any(), any()) }

        assertTrue(result.isFailure)
        assertEquals(exception.message, result.exceptionOrNull()?.message)
    }


    @Test
    fun `should return failure when writing to file throws exception`() {
        // When
        val state = State(UUID.randomUUID(), "TODO")
        val states = listOf<State>()
        val exception = RuntimeException("Write failed")

        // Given
        every { reader.read(any()) } returns states
        every { writer.writeToFile(states + state, filePath) } throws exception

        // Then
        val result = dataSource.createState(state)

        verify(exactly = 1) { reader.read(any()) }
        verify(exactly = 1) { writer.writeToFile(states + state, filePath) }

        assertTrue(result.isFailure)
        assertEquals(exception.message, result.exceptionOrNull()?.message)
    }

 //endregion

// region edit state tests
    @Test
    fun `should edit existing state and write updated list to file`() {
        // Given
        val oldState = State(UUID.randomUUID(), "Old Name")
        val updatedState = oldState.copy(name = "Updated Name")
        val existingStates = listOf(oldState)

        every { reader.read(any()) } returns existingStates
        every { writer.writeToFile(listOf(updatedState), filePath) } just Runs

        // When
        val result = dataSource.editState(updatedState)

        // Then
        verify(exactly = 1) { writer.writeToFile(listOf(updatedState), filePath) }
        assertTrue(result.isSuccess)
        assertEquals(updatedState, result.getOrNull())
    }

    @Test
    fun `should fail if state to edit does not exist`() {
        // Given
        val existingState = State(UUID.randomUUID(), "First")
        val newState = State(UUID.randomUUID(), "New")

        every { reader.read(any()) } returns listOf(existingState)

        // When
        val result = dataSource.editState(newState)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `should fail if file is empty and state not found`() {
        // Given
        val newState = State(UUID.randomUUID(), "TODO")
        every { reader.read(any()) } returns emptyList()

        // When
        val result = dataSource.editState(newState)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `should fail if writing to file throws exception`() {
        // Given
        val oldState = State(UUID.randomUUID(), "Old")
        val updatedState = oldState.copy(name = "Updated")

        every { reader.read(any()) } returns listOf(oldState)
        every { writer.writeToFile(any(), any()) } throws RuntimeException("Some error")

        // When
        val result = dataSource.editState(updatedState)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `should fail if file does not exist and state not found`() {
        // Given
        val newState = State(UUID.randomUUID(), "IN PROGRESS")
        File(filePath).delete()

        every { reader.read(emptyList()) } returns emptyList()

        // When
        val result = dataSource.editState(newState)

        // Then
        assertTrue(result.isFailure)
    }


// endregion

// region delete state tests

    @Test
    fun `should delete existing state and write updated list to file`() {
        // Given
        val stateToDelete = State(UUID.randomUUID(), "TODO")
        val otherState = State(UUID.randomUUID(), "IN PROGRESS")
        val existingStates = listOf(stateToDelete, otherState)

        every { reader.read(any()) } returns existingStates
        every { writer.writeToFile(listOf(otherState), filePath) } just Runs

        // When
        val result = dataSource.deleteState(stateToDelete.id)

        // Then
        verify(exactly = 1) { reader.read(any()) }
        verify(exactly = 1) { writer.writeToFile(listOf(otherState), filePath) }

        assertTrue(result.isSuccess)

    }

    @Test
    fun `should fail to delete if state not found`() {
        // Given
        val existingState = State(UUID.randomUUID(), "TODO")
        val nonExistentId = UUID.randomUUID()

        every { reader.read(any()) } returns listOf(existingState)

        // When
        val result = dataSource.deleteState(nonExistentId)

        // Then
        verify(exactly = 1) { reader.read(any()) }
        verify(exactly = 0) { writer.writeToFile(any(), any()) }

        assertTrue(result.isFailure)
    }

    @Test
    fun `should return failure when exception thrown while reading file`() {
        // Given
        val id = UUID.randomUUID()
        every { reader.read(any()) } throws RuntimeException("File read error")

        // When
        val result = dataSource.deleteState(id)

        // Then
        verify(exactly = 1) { reader.read(any()) }
        verify(exactly = 0) { writer.writeToFile(any(), any()) }

        assertTrue(result.isFailure)
        assertEquals("File read error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `should return failure when exception thrown while writing file`() {
        // Given
        val stateToDelete = State(UUID.randomUUID(), "TODO")
        val otherState = State(UUID.randomUUID(), "IN PROGRESS")
        val existingStates = listOf(stateToDelete, otherState)

        every { reader.read(any()) } returns existingStates
        every { writer.writeToFile(listOf(otherState), filePath) } throws RuntimeException("Write error")

        // When
        val result = dataSource.deleteState(stateToDelete.id)

        // Then
        verify(exactly = 1) { reader.read(any()) }
        verify(exactly = 1) { writer.writeToFile(listOf(otherState), filePath) }

        assertTrue(result.isFailure)
        assertEquals("Write error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `should fail to delete when file does not exist`() {
        // Given
        val nonExistentId = UUID.randomUUID()
        File(filePath).delete()

        every { reader.read(emptyList()) } returns emptyList()

        // When
        val result = dataSource.deleteState(nonExistentId)

        // Then
        verify(exactly = 1) { reader.read(emptyList()) }
        verify(exactly = 0) { writer.writeToFile(any(), any()) }

        assertTrue(result.isFailure)
    }

// endregion




}
