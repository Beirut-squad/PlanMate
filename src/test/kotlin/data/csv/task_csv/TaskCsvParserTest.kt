package data.csv.task_csv

import com.google.common.truth.Truth.assertThat
import creator_helper.createTaskHelper
import io.mockk.every
import io.mockk.mockk
import org.example.data.csv.state_csv.StateCsvParser
import org.example.data.csv.task_csv_parser.TaskCsvParser
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.util.*

class TaskCsvParserTest{

    private lateinit var stateCsvParser: StateCsvParser
    private lateinit var taskCsvParser: TaskCsvParser

    @BeforeEach
    fun setup(){
        stateCsvParser  = mockk(relaxed = true)
        taskCsvParser = TaskCsvParser(stateCsvParser)
    }

    @Test
    fun `parseLine should return null when the string is empty `(){

        // When
        val result = taskCsvParser.parseLine("[      ]")

        // Then
        assertThat(result).isNull()

    }

    @Test
    fun `parseLine should return a Task object when the string is not empty `(){

        // When
        val taskString = "[d1234567-89ab-cdef-0123-456789abcdef, d1234567-89ab-cdef-0123-456789abcdef ,Project Name,Project Description, [5481551e-2b45-49a0-b5fc-123456789012 , name], 5481551e-2b45-49a0-b5fc-123456789012 ,2024-04-01T12:00:00,2024-04-02T12:00:00]"
        val state = stateCsvParser.parseLine("[5481551e-2b45-49a0-b5fc-123456789012 , name]")
        val result = taskCsvParser.parseLine(taskString)

        // then

        assertThat(result).isEqualTo(
            createTaskHelper(
                UUID.fromString("d1234567-89ab-cdef-0123-456789abcdef"),
                UUID.fromString("d1234567-89ab-cdef-0123-456789abcdef"),
                "Project Name",
                "Project Description",
                state!!,
                UUID.fromString("5481551e-2b45-49a0-b5fc-123456789012"),
                LocalDateTime.parse("2024-04-01T12:00:00"),
                LocalDateTime.parse("2024-04-02T12:00:00")
            )
        )
    }

    @Test
    fun ` parseFile should return empty list if there is no csv lines`() {

        // When
        val result = taskCsvParser.parseFile(emptyList())

        // Then
        assertThat(result).isEmpty()
    }


    @Test
    fun `should return a list of tasks when csv contains lines `(){

        // When
        val taskString1 = "[d1234567-89ab-cdef-0123-456789abcdef, d1234567-89ab-cdef-0123-456789abcdef ,Project Name,Project Description, [5481551e-2b45-49a0-b5fc-123456789012 , name], 5481551e-2b45-49a0-b5fc-123456789012 ,2024-04-01T12:00:00,2024-04-02T12:00:00]"
        val taskString2 = "[d1234567-89ab-cdef-0123-456789abcdef, d1234567-89ab-cdef-0123-456789abcdef ,Project Name,Project Description, [5481551e-2b45-49a0-b5fc-123456789012 , name], 5481551e-2b45-49a0-b5fc-123456789012 ,2024-04-01T12:00:00,2024-04-02T12:00:00]"
        val taskString3 = "[   ]"
        val taskString4 = "[d1234567-89ab-cdef-0123-456789abcdef, d1234567-89ab-cdef-0123-456789abcdef ,Project Name,Project Description, [5481551e-2b45-49a0-b5fc-123456789012 , name], 5481551e-2b45-49a0-b5fc-123456789012 ,2024-04-01T12:00:00,2024-04-02T12:00:00]"
        val csvLines = listOf(taskString1,taskString2,taskString3,taskString4)


        val state = stateCsvParser.parseLine("[5481551e-2b45-49a0-b5fc-123456789012 , name]")
        val result = taskCsvParser.parseFile(csvLines)

        // Then
        assertThat(result).isEqualTo(
            listOf(
                createTaskHelper(
                    UUID.fromString("d1234567-89ab-cdef-0123-456789abcdef"),
                    UUID.fromString("d1234567-89ab-cdef-0123-456789abcdef"),
                    "Project Name",
                    "Project Description",
                    state!!,
                    UUID.fromString("5481551e-2b45-49a0-b5fc-123456789012"),
                    LocalDateTime.parse("2024-04-01T12:00:00"),
                    LocalDateTime.parse("2024-04-02T12:00:00")
                ),
                createTaskHelper(
                    UUID.fromString("d1234567-89ab-cdef-0123-456789abcdef"),
                    UUID.fromString("d1234567-89ab-cdef-0123-456789abcdef"),
                    "Project Name",
                    "Project Description",
                    state,
                    UUID.fromString("5481551e-2b45-49a0-b5fc-123456789012"),
                    LocalDateTime.parse("2024-04-01T12:00:00"),
                    LocalDateTime.parse("2024-04-02T12:00:00")
                ),
                createTaskHelper(
                    UUID.fromString("d1234567-89ab-cdef-0123-456789abcdef"),
                    UUID.fromString("d1234567-89ab-cdef-0123-456789abcdef"),
                    "Project Name",
                    "Project Description",
                    state,
                    UUID.fromString("5481551e-2b45-49a0-b5fc-123456789012"),
                    LocalDateTime.parse("2024-04-01T12:00:00"),
                    LocalDateTime.parse("2024-04-02T12:00:00")
                )
            )
        )
    }

    @Test
    fun `should throw exception if the stat of task is null `(){
        // Given
        every {
            stateCsvParser.parseLine(match {
                val cleaned = it.trim()
                cleaned.isEmpty() || cleaned.matches(Regex("""\[[ \t]*]"""))
            })
        } returns null

        // When
        val taskString = "[d1234567-89ab-cdef-0123-456789abcdef, d1234567-89ab-cdef-0123-456789abcdef ,Project Name,Project Description,[], 5481551e-2b45-49a0-b5fc-123456789012 ,2024-04-01T12:00:00,2024-04-02T12:00:00]"

        // then
        assertThrows<Exception> {
            taskCsvParser.parseLine(taskString)
        }
    }
}