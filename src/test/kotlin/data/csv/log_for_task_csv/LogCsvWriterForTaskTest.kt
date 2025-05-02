package data.csv.log_for_task_csv

import creator_helper.createTaskLogHelper
import io.mockk.mockk
import org.example.data.csv.log_for_task_csv.LogCsvWriterForTask
import org.example.models.ProjectLog
import org.example.models.TaskLog
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import java.io.File
import java.time.LocalDateTime
import java.util.*
import kotlin.test.Test

class LogCsvWriterForTaskTest{
    private lateinit var logCsvWriterForTask: LogCsvWriterForTask
    private lateinit var filePath: String

    @BeforeEach
    fun setUp(){
        logCsvWriterForTask = LogCsvWriterForTask()
        filePath = "test_TaskLog.csv"
    }
    @Test
    fun `given empty list of Tasks when writeToFile is called then file should contain only header`(){
        //Given
        val listOfTaskLogs = emptyList<TaskLog>()
        val file = File(filePath)
        assertTrue(file.length() == 0L)

        //when
        logCsvWriterForTask.writeToFile(listOfTaskLogs,filePath)

        val content = file.readText()
        //then

        assertTrue(file.exists())
        assertTrue(content.contains("id,userId,entityId,previousEntity,currentEntity,createdAt"))
        assertTrue(content.lines().size == 2)
        assertTrue(content.lines()[1].isBlank())
        file.delete()
    }

    @Test
    fun `given list of Tasks when writeToFile is called then file should be created`(){
        //Given
        val listOfTaskLogs = listOf(createTaskLogHelper(), createTaskLogHelper())
        val file = File(filePath)

        //when
        logCsvWriterForTask.writeToFile(listOfTaskLogs,filePath)

        //then

        assertTrue(file.exists())
        file.delete()
    }

    @Test
    fun `given invalid file name when writeToFile is called then return failure`() {
        //Given
        val invalidFilePath = "invalid|file.csv"
        val listOfTaskLogs = listOf(createTaskLogHelper(), createTaskLogHelper())

        //when
        val result = logCsvWriterForTask.writeToFile(listOfTaskLogs,invalidFilePath)

        //then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `given TaskLog with invalid ID when isValidTaskLog is called then should return false`() {
        //Given
        val taskLog = createTaskLogHelper(id = UUID(0,0))

        //When
        val result = logCsvWriterForTask.isValidTaskLog(taskLog)

        assertFalse(result)
    }

    @Test
    fun `given TaskLog with invalid userId when isValidTaskLog is called then should return false`() {
        //Given
        val taskLog = createTaskLogHelper(userId = UUID(0,0))

        //When
        val result = logCsvWriterForTask.isValidTaskLog(taskLog)

        assertFalse(result)
    }

    @Test
    fun `given TaskLog with invalid entityId when isValidTaskLog is called then should return false`() {
        //Given
        val taskLog = createTaskLogHelper(entityId = UUID(0,0))

        //When
        val result = logCsvWriterForTask.isValidTaskLog(taskLog)

        assertFalse(result)
    }

    @Test
    fun `given TaskLog with empty previousEntity when isValidTaskLog is called then should return true`() {
        //Given
        val taskLog = createTaskLogHelper(previousEntity = null)

        //When
        val result = logCsvWriterForTask.isValidTaskLog(taskLog)

        assertTrue(result)
    }

    @Test
    fun `given TaskLog with empty currentEntity when isValidTaskLog is called then should return true`() {
        //Given
        val taskLog = createTaskLogHelper(currentEntity =null)

        //When
        val result = logCsvWriterForTask.isValidTaskLog(taskLog)

        assertTrue(result)
    }

    @Test
    fun `given invalid Task log when writeToFile is called then it should be skipped`() {
        val invalidLog = createTaskLogHelper(id = UUID(0, 0))
        val validLog = createTaskLogHelper()
        val logs = listOf(invalidLog, validLog)

        val filePath = "test_TaskLog.csv"
        val writer = LogCsvWriterForTask()
        writer.writeToFile(logs, filePath)

        val file = File(filePath)
        val lines = file.readLines()

        assertEquals(2, lines.size)
        assertTrue(lines[1].contains(validLog.id.toString()))
        file.delete()
    }

}