package data.csv.log_for_task_csv

import com.google.common.truth.Truth.assertThat
import creator_helper.createTaskLogHelper
import io.mockk.every
import io.mockk.mockk
import org.example.data.csv.log_for_task_csv.LogCsvParserForTask
import data.csv.task_csv.TaskCsvParser
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class LogCsvParserForTaskTest{

    private lateinit var taskCsvParser: TaskCsvParser
    private lateinit var logCsvParserForTask: LogCsvParserForTask


    @BeforeEach
    fun setup(){
        taskCsvParser = mockk(relaxed = true)
        logCsvParserForTask = LogCsvParserForTask(taskCsvParser)
    }

    @Test
    fun `parseLine should return null when the string is empty `(){
        // When
        val result = logCsvParserForTask.parseLine("[      ]")

        // Then
        assertThat(result).isNull()

    }

    @Test
    fun `parseLine should return null when the string is empty without square brackets `(){
        // When
        val result = logCsvParserForTask.parseLine("   ")

        // Then
        assertThat(result).isNull()

    }

    @Test
    fun `parseLine should return a Task log object when the string is not empty `(){
        // When
        val sampleLine =
            "6565e2e3-91c1-4b3a-86e8-ffb0756b2c1f" + "," + "5481551e-2b45-49a0-b5fc-123456789012"+ "," + "5481551e-2b45-49a0-b5fc-123456789012"+ "," +
                    "[d1234567-89ab-cdef-0123-456789abcdef, Task Name,Task Description, 5481551e-2b45-49a0-b5fc-123456789012 ,2024-04-01T12:00:00,2024-04-02T12:00:00 ,[[5481551e-2b45-49a0-b5fc-123456789012 , name] , [5481551e-2b45-49a0-b5fc-123456789012 , name]]]"+","+
                    "[d1234567-89ab-cdef-0123-456789abcdef, Task Name,Task Description, 5481551e-2b45-49a0-b5fc-123456789012 ,2024-04-01T12:00:00,2024-04-02T12:00:00 ,[[5481551e-2b45-49a0-b5fc-123456789012 , name] , [5481551e-2b45-49a0-b5fc-123456789012 , name]]]"+","+
                    "2025-04-28T12:00:00"
        val taskString = "[d1234567-89ab-cdef-0123-456789abcdef, Task Name,Task Description, 5481551e-2b45-49a0-b5fc-123456789012 ,2024-04-01T12:00:00,2024-04-02T12:00:00 ,[[5481551e-2b45-49a0-b5fc-123456789012 , name] , [5481551e-2b45-49a0-b5fc-123456789012 , name]]]"
        val task = taskCsvParser.parseLine(taskString)
        val result = logCsvParserForTask.parseLine(sampleLine)

        // then
        assertThat(result).isEqualTo(
            createTaskLogHelper(
                UUID.fromString("6565e2e3-91c1-4b3a-86e8-ffb0756b2c1f"),
                UUID.fromString("5481551e-2b45-49a0-b5fc-123456789012"),
                UUID.fromString("5481551e-2b45-49a0-b5fc-123456789012"),
                task,
                task,
                LocalDateTime.parse("2025-04-28T12:00:00")
            )
        )
    }

    @Test
    fun ` parseFile should return empty list if there is no csv lines`() {
        // When
        val result = logCsvParserForTask.parseFile(emptyList())

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should return a list of Tasks when csv contains lines `(){
        // When
        val sampleLine1 =
            "6565e2e3-91c1-4b3a-86e8-ffb0756b2c1f" + "," + "5481551e-2b45-49a0-b5fc-123456789012"+ "," + "5481551e-2b45-49a0-b5fc-123456789012"+ "," +
                    "[d1234567-89ab-cdef-0123-456789abcdef, Task Name,Task Description, 5481551e-2b45-49a0-b5fc-123456789012 ,2024-04-01T12:00:00,2024-04-02T12:00:00 ,[[5481551e-2b45-49a0-b5fc-123456789012 , name] , [5481551e-2b45-49a0-b5fc-123456789012 , name]]]"+","+
                    "[d1234567-89ab-cdef-0123-456789abcdef, Task Name,Task Description, 5481551e-2b45-49a0-b5fc-123456789012 ,2024-04-01T12:00:00,2024-04-02T12:00:00 ,[[5481551e-2b45-49a0-b5fc-123456789012 , name] , [5481551e-2b45-49a0-b5fc-123456789012 , name]]]"+","+
                    "2025-04-28T12:00:00"
        val csvLines = listOf(sampleLine1,sampleLine1,"[   ]",sampleLine1)

        val taskString = "[d1234567-89ab-cdef-0123-456789abcdef, Task Name,Task Description, 5481551e-2b45-49a0-b5fc-123456789012 ,2024-04-01T12:00:00,2024-04-02T12:00:00 ,[[5481551e-2b45-49a0-b5fc-123456789012 , name] , [5481551e-2b45-49a0-b5fc-123456789012 , name]]]"
        val task = taskCsvParser.parseLine(taskString)
        val result = logCsvParserForTask.parseFile(csvLines)

        // Then
        assertThat(result).isEqualTo(
            listOf(
                createTaskLogHelper(
                    UUID.fromString("6565e2e3-91c1-4b3a-86e8-ffb0756b2c1f"),
                    UUID.fromString("5481551e-2b45-49a0-b5fc-123456789012"),
                    UUID.fromString("5481551e-2b45-49a0-b5fc-123456789012"),
                    task,
                    task,
                    LocalDateTime.parse("2025-04-28T12:00:00")
                ),
                createTaskLogHelper(
                    UUID.fromString("6565e2e3-91c1-4b3a-86e8-ffb0756b2c1f"),
                    UUID.fromString("5481551e-2b45-49a0-b5fc-123456789012"),
                    UUID.fromString("5481551e-2b45-49a0-b5fc-123456789012"),
                    task,
                    task,
                    LocalDateTime.parse("2025-04-28T12:00:00")
                ),
                createTaskLogHelper(
                    UUID.fromString("6565e2e3-91c1-4b3a-86e8-ffb0756b2c1f"),
                    UUID.fromString("5481551e-2b45-49a0-b5fc-123456789012"),
                    UUID.fromString("5481551e-2b45-49a0-b5fc-123456789012"),
                    task,
                    task,
                    LocalDateTime.parse("2025-04-28T12:00:00")
                )
            )
        )
    }

    @Test
    fun `should throw exception if the Task previous entity of log is null `(){
        // Given
        every {
            taskCsvParser.parseLine(match {
                val cleaned = it.trim()
                cleaned.isEmpty() || cleaned.matches(Regex("""\[[ \t]*]"""))
            })
        } returns null

        // When
        val sampleLine =
            "6565e2e3-91c1-4b3a-86e8-ffb0756b2c1f" + "," + "5481551e-2b45-49a0-b5fc-123456789012"+ "," + "5481551e-2b45-49a0-b5fc-123456789012"+ "," +
                    "[]"+","+
                    "[d1234567-89ab-cdef-0123-456789abcdef, Task Name,Task Description, 5481551e-2b45-49a0-b5fc-123456789012 ,2024-04-01T12:00:00,2024-04-02T12:00:00 ,[[5481551e-2b45-49a0-b5fc-123456789012 , name] , [5481551e-2b45-49a0-b5fc-123456789012 , name]]]"+","+
                    "2025-04-28T12:00:00"
        // then
        org.junit.jupiter.api.assertThrows<Exception> {
            logCsvParserForTask.parseLine(sampleLine)
        }
    }

    @Test
    fun `should throw exception if the Task current entity of log is null `(){
        // Given
        every {
            taskCsvParser.parseLine(match {
                val cleaned = it.trim()
                cleaned.isEmpty() || cleaned.matches(Regex("""\[[ \t]*]"""))
            })
        } returns null

        // When
        val sampleLine =
            "6565e2e3-91c1-4b3a-86e8-ffb0756b2c1f" + "," + "5481551e-2b45-49a0-b5fc-123456789012"+ "," + "5481551e-2b45-49a0-b5fc-123456789012"+ "," +
                    "[d1234567-89ab-cdef-0123-456789abcdef, Task Name,Task Description, 5481551e-2b45-49a0-b5fc-123456789012 ,2024-04-01T12:00:00,2024-04-02T12:00:00 ,[[5481551e-2b45-49a0-b5fc-123456789012 , name] , [5481551e-2b45-49a0-b5fc-123456789012 , name]]]"+","+
                    "[]"+","+
                    "2025-04-28T12:00:00"
        // then
        org.junit.jupiter.api.assertThrows<Exception> {
            logCsvParserForTask.parseLine(sampleLine)
        }
    }

    @Test
    fun `should throw exception if both Task current entity and previous entity of log are null `(){
        // Given
        every {
            taskCsvParser.parseLine(match {
                val cleaned = it.trim()
                cleaned.isEmpty() || cleaned.matches(Regex("""\[[ \t]*]"""))
            })
        } returns null

        // When
        val sampleLine =
            "6565e2e3-91c1-4b3a-86e8-ffb0756b2c1f" + "," + "5481551e-2b45-49a0-b5fc-123456789012"+ "," + "5481551e-2b45-49a0-b5fc-123456789012"+ "," +
                    "[]"+","+
                    "[]"+","+
                    "2025-04-28T12:00:00"
        // then
        org.junit.jupiter.api.assertThrows<Exception> {
            logCsvParserForTask.parseLine(sampleLine)
        }
    }
}