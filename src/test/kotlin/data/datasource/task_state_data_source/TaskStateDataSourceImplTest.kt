package data.datasource.task_state_data_source

import io.mockk.*
import org.example.data.csv.CsvReader
import org.example.data.csv.CsvWriter
import org.example.data.datasource.task_state_data_source.TaskStateDataSourceImpl
import org.example.models.TaskState
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TaskStateDataSourceImplTest {

    private lateinit var writer: CsvWriter<TaskState>
    private lateinit var reader: CsvReader<TaskState>
    private lateinit var dataSource: TaskStateDataSourceImpl
    private lateinit var filePath: String

    @BeforeEach
    fun setUp() {
        writer = mockk(relaxed = true)
        reader = mockk()
        filePath = "test_states.csv"
        dataSource = TaskStateDataSourceImpl(writer, reader, filePath)

        File(filePath).delete()
    }
// region add state tests
    @Test
    fun `should create state and write it to file when file is empty`() {
        // When
        val taskState = TaskState(UUID.randomUUID(), "TODO")
        val taskStates = listOf<TaskState>()

        // Given
        every { reader.read(any()) } returns taskStates
        every { writer.writeToFile(any(), filePath) } returns Result.success(Unit)


    // Then
        val result = dataSource.createTaskState(taskState)

        verify(exactly = 1) { reader.read(any()) }
        verify(exactly = 1) { writer.writeToFile(taskStates + taskState, filePath) }

        assertTrue(result.isSuccess)
        assertEquals(taskState, result.getOrNull())
    }

    @Test
    fun `should read from existing file and append new state`() {
        val taskState = TaskState(UUID.randomUUID(), "TODO")
        val states = TaskState(UUID.randomUUID(), "Existing")

        File(filePath).writeText("text")

        every { reader.read(any()) } returns listOf(states)
        every { writer.writeToFile(listOf(states, taskState), filePath) } returns
                Result.success(Unit)


        val result = dataSource.createTaskState(taskState)

        verify(exactly = 1) { reader.read(any()) }
        verify(exactly = 1) { writer.writeToFile(listOf(states, taskState), filePath) }

        assertTrue(result.isSuccess)
        assertEquals(taskState, result.getOrNull())
    }

    @Test
    fun `should return failure when parsing file throws exception`() {
        // When
        val taskState = TaskState(UUID.randomUUID(), "TODO")
        val exception = RuntimeException("Parsing failed")

        // Given
        every { reader.read(any()) } throws exception

        // Then
        val result = dataSource.createTaskState(taskState)

        verify(exactly = 1) { reader.read(any()) }
        verify(exactly = 0) { writer.writeToFile(any(), any()) }

        assertTrue(result.isFailure)
        assertEquals(exception.message, result.exceptionOrNull()?.message)
    }


    @Test
    fun `should return failure when writing to file throws exception`() {
        // When
        val taskState = TaskState(UUID.randomUUID(), "TODO")
        val taskStates = listOf<TaskState>()
        val exception = RuntimeException("Write failed")

        // Given
        every { reader.read(any()) } returns taskStates
        every { writer.writeToFile(taskStates + taskState, filePath) } throws exception

        // Then
        val result = dataSource.createTaskState(taskState)

        verify(exactly = 1) { reader.read(any()) }
        verify(exactly = 1) { writer.writeToFile(taskStates + taskState, filePath) }

        assertTrue(result.isFailure)
        assertEquals(exception.message, result.exceptionOrNull()?.message)
    }

 //endregion

// region edit state tests
    @Test
    fun `should edit existing state and write updated list to file`() {
        // Given
        val oldTaskState = TaskState(UUID.randomUUID(), "Old Name")
        val updatedState = oldTaskState.copy(name = "Updated Name")
        val existingStates = listOf(oldTaskState)

        every { reader.read(any()) } returns existingStates
        every { writer.writeToFile(listOf(updatedState), filePath) } returns
                Result.success(Unit)


    // When
        val result = dataSource.editTaskState(updatedState)

        // Then
        verify(exactly = 1) { writer.writeToFile(listOf(updatedState), filePath) }
        assertTrue(result.isSuccess)
        assertEquals(updatedState, result.getOrNull())
    }

    @Test
    fun `should fail if state to edit does not exist`() {
        // Given
        val existingTaskState = TaskState(UUID.randomUUID(), "First")
        val newTaskState = TaskState(UUID.randomUUID(), "New")

        every { reader.read(any()) } returns listOf(existingTaskState)

        // When
        val result = dataSource.editTaskState(newTaskState)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `should fail if file is empty and state not found`() {
        // Given
        val newTaskState = TaskState(UUID.randomUUID(), "TODO")
        every { reader.read(any()) } returns emptyList()

        // When
        val result = dataSource.editTaskState(newTaskState)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `should fail if writing to file throws exception`() {
        // Given
        val oldTaskState = TaskState(UUID.randomUUID(), "Old")
        val updatedState = oldTaskState.copy(name = "Updated")

        every { reader.read(any()) } returns listOf(oldTaskState)
        every { writer.writeToFile(any(), any()) } throws RuntimeException("Some error")

        // When
        val result = dataSource.editTaskState(updatedState)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `should fail if file does not exist and state not found`() {
        // Given
        val newTaskState = TaskState(UUID.randomUUID(), "IN PROGRESS")
        File(filePath).delete()

        every { reader.read(filePath) } returns emptyList()

        // When
        val result = dataSource.editTaskState(newTaskState)

        // Then
        assertTrue(result.isFailure)
    }


// endregion

// region delete state tests

    @Test
    fun `should delete existing state and write updated list to file`() {
        // Given
        val taskStateToDelete = TaskState(UUID.randomUUID(), "TODO")
        val otherTaskState = TaskState(UUID.randomUUID(), "IN PROGRESS")
        val existingStates = listOf(taskStateToDelete, otherTaskState)

        every { reader.read(any()) } returns existingStates
        every { writer.writeToFile(listOf(otherTaskState), filePath) } returns
                Result.success(Unit)


        // When
        val result = dataSource.deleteTaskState(taskStateToDelete.id)

        // Then
        verify(exactly = 1) { reader.read(any()) }
        verify(exactly = 1) { writer.writeToFile(listOf(otherTaskState), filePath) }

        assertTrue(result.isSuccess)

    }

    @Test
    fun `should fail to delete if state not found`() {
        // Given
        val existingTaskState = TaskState(UUID.randomUUID(), "TODO")
        val nonExistentId = UUID.randomUUID()

        every { reader.read(any()) } returns listOf(existingTaskState)

        // When
        val result = dataSource.deleteTaskState(nonExistentId)

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
        val result = dataSource.deleteTaskState(id)

        // Then
        verify(exactly = 1) { reader.read(any()) }
        verify(exactly = 0) { writer.writeToFile(any(), any()) }

        assertTrue(result.isFailure)
        assertEquals("File read error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `should return failure when exception thrown while writing file`() {
        // Given
        val taskStateToDelete = TaskState(UUID.randomUUID(), "TODO")
        val otherTaskState = TaskState(UUID.randomUUID(), "IN PROGRESS")
        val existingStates = listOf(taskStateToDelete, otherTaskState)

        every { reader.read(any()) } returns existingStates
        every { writer.writeToFile(listOf(otherTaskState), filePath) } throws RuntimeException("Write error")

        // When
        val result = dataSource.deleteTaskState(taskStateToDelete.id)

        // Then
        verify(exactly = 1) { reader.read(any()) }
        verify(exactly = 1) { writer.writeToFile(listOf(otherTaskState), filePath) }

        assertTrue(result.isFailure)
        assertEquals("Write error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `should fail to delete when file does not exist`() {
        // Given
        val nonExistentId = UUID.randomUUID()
        File(filePath).delete()

        every { reader.read(filePath) } returns emptyList()

        // When
        val result = dataSource.deleteTaskState(nonExistentId)

        // Then
        verify(exactly = 1) { reader.read(filePath) }
        verify(exactly = 0) { writer.writeToFile(any(), any()) }

        assertTrue(result.isFailure)
    }

// endregion




}
