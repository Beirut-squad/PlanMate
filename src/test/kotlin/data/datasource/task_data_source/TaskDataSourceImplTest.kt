package data.datasource.task_data_source

import creator_helper.createTaskHelper
import io.mockk.*
import org.example.data.csv.CsvReader
import org.example.data.csv.CsvWriter
import org.example.data.datasource.task_data_source.TaskDataSourceImpl
import org.example.logic.exceptions.GetAllTasksException
import org.example.logic.exceptions.GetTaskException
import org.example.logic.exceptions.TaskCreationException
import org.example.logic.exceptions.TaskDeletionException
import org.example.logic.exceptions.TaskEditException
import org.example.models.Task
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertFailsWith

class TaskDataSourceImplTest {
    private var csvReader: CsvReader<Task> = mockk(relaxed = true)
    private var csvWriter: CsvWriter<Task> = mockk(relaxed = true)
    private lateinit var taskdataSource: TaskDataSourceImpl

    @BeforeEach
    fun setUp() {
        taskdataSource = TaskDataSourceImpl(csvReader, csvWriter)
    }

    @Test
    fun `createTask adds task and writes updated list`() {
        // Given
        val existingTasks = listOf<Task>()
        val newTask = createTaskHelper()

        every { csvReader.read(TaskDataSourceImpl.TASK_FILE) } returns existingTasks
        every { csvWriter.writeToFile(any(), TaskDataSourceImpl.TASK_FILE) } just Runs

        // When
        val result = taskdataSource.createTask(newTask)

        // Then
        Assertions.assertTrue(result.isSuccess)
        verify {
            csvWriter.writeToFile(
                match { it.contains(newTask) && it.size == 1 },
                TaskDataSourceImpl.TASK_FILE
            )
        }
    }

    @Test
    fun `createTask returns TaskCreationException when csvWriter fails`() {
        // Given
        val existingTasks = listOf<Task>()
        val newTask = createTaskHelper()
        val exception = TaskCreationException("Failed to write to file ")

        every { csvReader.read(TaskDataSourceImpl.TASK_FILE) } returns existingTasks
        every { csvWriter.writeToFile(any(), TaskDataSourceImpl.TASK_FILE) } throws exception

        // When
        val result = taskdataSource.createTask(newTask)

        // Then
        Assertions.assertTrue(result.isFailure)
        Assertions.assertTrue(result.exceptionOrNull() is TaskCreationException)
        verify { csvReader.read(TaskDataSourceImpl.TASK_FILE) }
        verify(exactly = 1) { csvWriter.writeToFile(any(), TaskDataSourceImpl.TASK_FILE) }
    }

    @Test
    fun `editTask updates existing task successfully`() {
        // Given
        val taskId = UUID.randomUUID()
        val existingTask = createTaskHelper(id = taskId)
        val updatedTask = createTaskHelper(id = taskId, title = "Updated Task", description = "Updated Description")

        val existingTasks = listOf(existingTask)
        every { csvReader.read(TaskDataSourceImpl.TASK_FILE) } returns existingTasks
        every { csvWriter.writeToFile(any(), TaskDataSourceImpl.TASK_FILE) } just Runs

        // When
        val result = taskdataSource.editTask(updatedTask)

        // Then
        assertTrue(result.isSuccess)
        verify {
            csvWriter.writeToFile(
                match { it.contains(updatedTask) && it.size == 1 },
                TaskDataSourceImpl.TASK_FILE
            )
        }
    }

    @Test
    fun `editTask returns failure if task not found`() {
        // Given
        val taskId = UUID.randomUUID()
        val nonExistentTask = createTaskHelper(id = taskId, title = "Updated Task", description = "Updated Description")

        val existingTasks = listOf<Task>()
        every { csvReader.read(TaskDataSourceImpl.TASK_FILE) } returns existingTasks

        // When
        val result = taskdataSource.editTask(nonExistentTask)

        // Then
        assertTrue(result.isFailure)
        Assertions.assertTrue(result.exceptionOrNull() is TaskEditException)
    }

    @Test
    fun `editTask should fail when task with ID is not found`() {
        // Given
        val task = createTaskHelper(id = UUID.randomUUID(), title = "Test Task", description = "Test Description")
        val existingTask = createTaskHelper(id = UUID.randomUUID(), title = "Existing Task", description = "Existing Description")
        every { csvReader.read(any()) } returns listOf(existingTask) // Returning a task list with one existing task

        // When & Then
        val result = taskdataSource.editTask(task)
        assertTrue(result.isFailure)
        Assertions.assertTrue(result.exceptionOrNull() is TaskEditException)
        verify(exactly = 0) { csvWriter.writeToFile(any(), any()) }
    }
    @Test
    fun `editTask should fail when task list is empty`() {
        // Given
        val task = createTaskHelper()
        every { csvReader.read(any()) } returns emptyList()
        // When
        val result = taskdataSource.editTask(task)
        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is TaskEditException)
        assertEquals("No tasks found to edit", result.exceptionOrNull()?.message)
        verify { csvReader.read(any()) }
        verify(exactly = 0) { csvWriter.writeToFile(any(), any()) }
    }
    @Test
    fun `editTask returns failure when an exception occurs`() {
        // Given
        val taskId = UUID.randomUUID()
        val taskToEdit = createTaskHelper(id = taskId, title = "Updated Task", description = "Updated Description")

        every { csvReader.read(TaskDataSourceImpl.TASK_FILE) } throws RuntimeException("Error reading file")

        // When
        val result = taskdataSource.editTask(taskToEdit)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is TaskEditException)
        assertEquals("Failed to edit task: Error reading file", result.exceptionOrNull()?.message)
    }


    @Test
    fun `deleteTask removes existing task successfully`() {
        // Given
        val taskId = UUID.randomUUID()
        val existingTask = createTaskHelper(id = taskId)
        val tasksList = listOf(existingTask)

        every { csvReader.read(TaskDataSourceImpl.TASK_FILE) } returns tasksList
        every { csvWriter.writeToFile(any(), TaskDataSourceImpl.TASK_FILE) } just Runs

        // When
        val result = taskdataSource.deleteTask(taskId)

        // Then
        assertTrue(result.isSuccess)
        verify {
            csvWriter.writeToFile(
                match { it.isEmpty() }, // Verifying that the list is empty after deletion
                TaskDataSourceImpl.TASK_FILE
            )
        }
    }
    @Test
    fun `deleteTask should fail when task list is empty`() {
        // Given
        val taskId = UUID.randomUUID()
        every { csvReader.read(any()) } returns emptyList()
        // When
        val result = taskdataSource.deleteTask(taskId)
        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is TaskDeletionException)
        assertEquals("No tasks found to delete", result.exceptionOrNull()?.message)
        verify { csvReader.read(any()) }
        verify(exactly = 0) { csvWriter.writeToFile(any(), any()) }
    }

    @Test
    fun `deleteTask returns failure if task not found`() {
        // Given
        val taskId = UUID.randomUUID()
        val existingTasks = listOf(createTaskHelper(), createTaskHelper())
        every { csvReader.read(TaskDataSourceImpl.TASK_FILE) } returns existingTasks

        // When
        val result = taskdataSource.deleteTask(taskId)

        // Then
        assertTrue(result.isFailure)
        Assertions.assertTrue(result.exceptionOrNull() is TaskDeletionException)
    }

    @Test
    fun `deleteTask returns failure if writing updated tasks fails`() {
        // Given
        val taskId = UUID.randomUUID()
        val existingTask = createTaskHelper(id = taskId)
        every { csvReader.read(TaskDataSourceImpl.TASK_FILE) } returns listOf(existingTask)
        every { csvWriter.writeToFile(any(), TaskDataSourceImpl.TASK_FILE) } throws RuntimeException("Write error")

        // When
        val result = taskdataSource.deleteTask(taskId)

        // Then
        assertTrue(result.isFailure)
        Assertions.assertTrue(result.exceptionOrNull() is TaskDeletionException)
    }

    @Test
    fun `getAllTasks returns all tasks successfully`() {
        // Given
        val tasks = listOf(
            createTaskHelper(title = "Task 1"),
            createTaskHelper(title = "Task 2"),
            createTaskHelper(title = "Task 3")
        )
        every { csvReader.read(TaskDataSourceImpl.TASK_FILE) } returns tasks

        // When
        val result = taskdataSource.getAllTasks()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(tasks, result.getOrNull())
        verify { csvReader.read(TaskDataSourceImpl.TASK_FILE) }
    }
    @Test
    fun `getAllTasks returns failure if reading tasks throws exception`() {
        // Given
        every { csvReader.read(TaskDataSourceImpl.TASK_FILE) } throws RuntimeException("Read error")

        // When
        val result = taskdataSource.getAllTasks()

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is GetAllTasksException)
    }

    @Test
    fun `getAllTasks returns failure when list is empty`() {
        every { csvReader.read(TaskDataSourceImpl.TASK_FILE) } returns emptyList()

        val result = taskdataSource.getAllTasks()

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is GetAllTasksException)
        assertEquals("No tasks found", result.exceptionOrNull()?.message)
    }

    @Test
    fun `getTaskById returns task successfully`() {
        // Given
        val taskId = UUID.randomUUID()
        val existingTask = createTaskHelper(id = taskId)
        val tasks = listOf(existingTask)

        every { csvReader.read(TaskDataSourceImpl.TASK_FILE) } returns tasks

        // When
        val result = taskdataSource.getTask(taskId)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(existingTask, result.getOrNull())
    }
    @Test
    fun `getTaskById returns failure when task not found`() {
        // Given
        val taskId = UUID.randomUUID()
        val tasks = listOf(createTaskHelper())

        every { csvReader.read(TaskDataSourceImpl.TASK_FILE) } returns tasks

        // When
        val result = taskdataSource.getTask(taskId)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is GetTaskException)
    }

    @Test
    fun `getTaskById returns failure when exception occurs`() {
        // Given
        val taskId = UUID.randomUUID()
        every { csvReader.read(TaskDataSourceImpl.TASK_FILE) } throws RuntimeException("Read failed")

        // When
        val result = taskdataSource.getTask(taskId)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is GetTaskException)
    }

}