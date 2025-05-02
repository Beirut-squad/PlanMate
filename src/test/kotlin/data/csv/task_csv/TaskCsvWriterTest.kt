package data.csv.task_csv

import creator_helper.createTaskHelper
import org.example.data.csv.state_csv.StateCsvWriter
import org.example.data.csv.task_csv_parser.TaskCsvWriter
import org.example.models.Task
import org.junit.jupiter.api.Assertions.*
import java.io.File
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test

class TaskCsvWriterTest{
  private lateinit var  taskCsvWriter : TaskCsvWriter
  private lateinit var  filePath: String


  @BeforeTest
  fun setup(){
   taskCsvWriter = TaskCsvWriter()
   filePath = "test_task.csv"
  }
    @Test
    fun `given list of task when writeToFile is called then should be created`() {
        // Given
        val tasks = listOf(createTaskHelper())

        // When
        taskCsvWriter.writeToFile(tasks, filePath)
        val file = File(filePath)

        // Then
        assertTrue(file.exists())
        file.delete()
    }

    @Test
    fun `given empty list of task when writeToFile is called then should create file with only header`() {
        // Given
        val tasks = emptyList<Task>()

        // When
        val file = File(filePath)
        assertTrue(file.length() == 0L)
        taskCsvWriter.writeToFile(tasks, filePath)

        // Then
        val lines = file.readLines()
        assertTrue(lines.first().contains("id,projectId,title,description,state,creatorUserID,createdAt,updatedAt"))
        assertTrue(file.exists())
        file.delete()
    }

    @Test
    fun `given list of task when writeToFile is called then should create file with header and task data`() {
        // Given
        val tasks = listOf(createTaskHelper(title = "Task 1"), createTaskHelper(title = "Task 2"))

        // When
        taskCsvWriter.writeToFile(tasks, filePath)
        val file = File(filePath)
        assertTrue(file.length() != 0L)
        val lines = file.readLines()

        // Then
        assertTrue(file.exists())
        assertTrue(lines[0].contains("id,projectId,title,description,state,creatorUserID,createdAt,updatedAt"))
        assertTrue(lines[1].contains("Task 1"))
        assertTrue(lines[2].contains("Task 2"))
        assertTrue(lines.size == 3)

        file.delete()
    }

    @Test
    fun `given invalid task with empty title when writeToFile is called then should not write invalid task`() {
        // Given
        val tasks = listOf(createTaskHelper(title = "Task 1"), createTaskHelper(title = ""))

        // When
        taskCsvWriter.writeToFile(tasks, filePath)
        val file = File(filePath)
        val lines = file.readLines()

        // Then
        assertTrue(file.exists())
        assertTrue(lines[0].contains("id,projectId,title,description,state,creatorUserID,createdAt,updatedAt"))
        assertTrue(lines[1].contains("Task 1"))
        assertTrue(lines.size == 2)

        file.delete()
    }

    @Test
    fun `given invalid file name when writeToFile is called then return failure`() {
        // Given
        val invalidFilePath = "invalid|file/na|me.csv"
        val tasks = listOf(createTaskHelper())

        // When
        val result = taskCsvWriter.writeToFile(tasks, invalidFilePath)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `isValidTask returns false when id is default UUID (0, 0)`() {
        val task = createTaskHelper(id = UUID(0, 0))
        assertFalse(taskCsvWriter.isValidTask(task))
    }
    @Test
    fun `isValidTask returns false when title is blank`() {
        val task = createTaskHelper(title = "")
        assertFalse(taskCsvWriter.isValidTask(task))
    }
    @Test
    fun `isValidTask returns false when description is blank`() {
        val task = createTaskHelper(description = "")
        assertFalse(taskCsvWriter.isValidTask(task))
    }
    @Test
    fun `isValidTask returns false when creatorUserID is default UUID (0, 0)`() {
        val task = createTaskHelper(creatorUserID = UUID(0, 0))
        assertFalse(taskCsvWriter.isValidTask(task))
    }
    @Test
    fun `isValidTask returns false when all fields are invalid`() {
        val task = createTaskHelper(
            id = UUID(0, 0),
            title = "",
            description = "",
            creatorUserID = UUID(0, 0)
        )
        assertFalse(taskCsvWriter.isValidTask(task))
    }

}