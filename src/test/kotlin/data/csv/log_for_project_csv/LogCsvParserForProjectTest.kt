package data.csv.log_for_project_csv

import com.google.common.truth.Truth.assertThat
import creator_helper.createProjectHelper
import creator_helper.createProjectLogHelper
import data.csv.project_csv.ProjectCsvParser
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class LogCsvParserForProjectTest{

    private lateinit var projectCsvParser: ProjectCsvParser
    private lateinit var logCsvParserForProject: LogCsvParserForProject


    @BeforeEach
    fun setup(){
        projectCsvParser = mockk(relaxed = true)
        logCsvParserForProject = LogCsvParserForProject(projectCsvParser)
    }

    @Test
    fun `parseLine should return null when the string is empty `(){
        // When
        val result = logCsvParserForProject.parseLine("[      ]")

        // Then
        assertThat(result).isNull()

    }

    @Test
    fun `parseLine should return a Project log object when the string is not empty `(){
        // When
        val sampleLine =
            "6565e2e3-91c1-4b3a-86e8-ffb0756b2c1f" + "," + "5481551e-2b45-49a0-b5fc-123456789012"+ "," + "5481551e-2b45-49a0-b5fc-123456789012"+ "," +
                    "[d1234567-89ab-cdef-0123-456789abcdef, Project Name,Project Description, 5481551e-2b45-49a0-b5fc-123456789012 ,2024-04-01T12:00:00,2024-04-02T12:00:00 ,[[5481551e-2b45-49a0-b5fc-123456789012 , name] , [5481551e-2b45-49a0-b5fc-123456789012 , name]]]"+","+
                    "[d1234567-89ab-cdef-0123-456789abcdef, Project Name,Project Description, 5481551e-2b45-49a0-b5fc-123456789012 ,2024-04-01T12:00:00,2024-04-02T12:00:00 ,[[5481551e-2b45-49a0-b5fc-123456789012 , name] , [5481551e-2b45-49a0-b5fc-123456789012 , name]]]"+","+
                    "2025-04-28T12:00:00"
        val projectString = "[d1234567-89ab-cdef-0123-456789abcdef, Project Name,Project Description, 5481551e-2b45-49a0-b5fc-123456789012 ,2024-04-01T12:00:00,2024-04-02T12:00:00 ,[[5481551e-2b45-49a0-b5fc-123456789012 , name] , [5481551e-2b45-49a0-b5fc-123456789012 , name]]]"
        val project = projectCsvParser.parseLine(projectString)
        val result = logCsvParserForProject.parseLine(sampleLine)

        // then
        assertThat(result).isEqualTo(
            createProjectLogHelper(
                UUID.fromString("6565e2e3-91c1-4b3a-86e8-ffb0756b2c1f"),
                UUID.fromString("5481551e-2b45-49a0-b5fc-123456789012"),
                UUID.fromString("5481551e-2b45-49a0-b5fc-123456789012"),
                project,
                project,
                LocalDateTime.parse("2025-04-28T12:00:00")
            )
        )
    }

    @Test
    fun ` parseFile should return empty list if there is no csv lines`() {
        // When
        val result = logCsvParserForProject.parseFile(emptyList())

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should return a list of Projects when csv contains lines `(){
        // When
        val sampleLine1 =
            "6565e2e3-91c1-4b3a-86e8-ffb0756b2c1f" + "," + "5481551e-2b45-49a0-b5fc-123456789012"+ "," + "5481551e-2b45-49a0-b5fc-123456789012"+ "," +
                    "[d1234567-89ab-cdef-0123-456789abcdef, Project Name,Project Description, 5481551e-2b45-49a0-b5fc-123456789012 ,2024-04-01T12:00:00,2024-04-02T12:00:00 ,[[5481551e-2b45-49a0-b5fc-123456789012 , name] , [5481551e-2b45-49a0-b5fc-123456789012 , name]]]"+","+
                    "[d1234567-89ab-cdef-0123-456789abcdef, Project Name,Project Description, 5481551e-2b45-49a0-b5fc-123456789012 ,2024-04-01T12:00:00,2024-04-02T12:00:00 ,[[5481551e-2b45-49a0-b5fc-123456789012 , name] , [5481551e-2b45-49a0-b5fc-123456789012 , name]]]"+","+
                    "2025-04-28T12:00:00"
        val csvLines = listOf(sampleLine1,sampleLine1,"[   ]",sampleLine1)

        val projectString = "[d1234567-89ab-cdef-0123-456789abcdef, Project Name,Project Description, 5481551e-2b45-49a0-b5fc-123456789012 ,2024-04-01T12:00:00,2024-04-02T12:00:00 ,[[5481551e-2b45-49a0-b5fc-123456789012 , name] , [5481551e-2b45-49a0-b5fc-123456789012 , name]]]"
        val project = projectCsvParser.parseLine(projectString)
        val result = logCsvParserForProject.parseFile(csvLines)

        // Then
        assertThat(result).isEqualTo(
            listOf(
                createProjectLogHelper(
                    UUID.fromString("6565e2e3-91c1-4b3a-86e8-ffb0756b2c1f"),
                    UUID.fromString("5481551e-2b45-49a0-b5fc-123456789012"),
                    UUID.fromString("5481551e-2b45-49a0-b5fc-123456789012"),
                    project,
                    project,
                    LocalDateTime.parse("2025-04-28T12:00:00")
                ),
                createProjectLogHelper(
                    UUID.fromString("6565e2e3-91c1-4b3a-86e8-ffb0756b2c1f"),
                    UUID.fromString("5481551e-2b45-49a0-b5fc-123456789012"),
                    UUID.fromString("5481551e-2b45-49a0-b5fc-123456789012"),
                    project,
                    project,
                    LocalDateTime.parse("2025-04-28T12:00:00")
                ),
                createProjectLogHelper(
                    UUID.fromString("6565e2e3-91c1-4b3a-86e8-ffb0756b2c1f"),
                    UUID.fromString("5481551e-2b45-49a0-b5fc-123456789012"),
                    UUID.fromString("5481551e-2b45-49a0-b5fc-123456789012"),
                    project,
                    project,
                    LocalDateTime.parse("2025-04-28T12:00:00")
                )
            )
        )
    }

    @Test
    fun `should throw exception if the project previous entity of log is null `(){
        // Given
        every {
            projectCsvParser.parseLine(match {
                val cleaned = it.trim()
                cleaned.isEmpty() || cleaned.matches(Regex("""\[[ \t]*]"""))
            })
        } returns null

        // When
        val sampleLine =
            "6565e2e3-91c1-4b3a-86e8-ffb0756b2c1f" + "," + "5481551e-2b45-49a0-b5fc-123456789012"+ "," + "5481551e-2b45-49a0-b5fc-123456789012"+ "," +
                    "[]"+","+
                    "[d1234567-89ab-cdef-0123-456789abcdef, Project Name,Project Description, 5481551e-2b45-49a0-b5fc-123456789012 ,2024-04-01T12:00:00,2024-04-02T12:00:00 ,[[5481551e-2b45-49a0-b5fc-123456789012 , name] , [5481551e-2b45-49a0-b5fc-123456789012 , name]]]"+","+
                    "2025-04-28T12:00:00"
        // then
        org.junit.jupiter.api.assertThrows<Exception> {
            logCsvParserForProject.parseLine(sampleLine)
        }
    }

    @Test
    fun `should throw exception if the project current entity of log is null `(){
        // Given
        every {
            projectCsvParser.parseLine(match {
                val cleaned = it.trim()
                cleaned.isEmpty() || cleaned.matches(Regex("""\[[ \t]*]"""))
            })
        } returns null

        // When
        val sampleLine =
            "6565e2e3-91c1-4b3a-86e8-ffb0756b2c1f" + "," + "5481551e-2b45-49a0-b5fc-123456789012"+ "," + "5481551e-2b45-49a0-b5fc-123456789012"+ "," +
                    "[d1234567-89ab-cdef-0123-456789abcdef, Project Name,Project Description, 5481551e-2b45-49a0-b5fc-123456789012 ,2024-04-01T12:00:00,2024-04-02T12:00:00 ,[[5481551e-2b45-49a0-b5fc-123456789012 , name] , [5481551e-2b45-49a0-b5fc-123456789012 , name]]]"+","+
                    "[]"+","+
                    "2025-04-28T12:00:00"
        // then
        org.junit.jupiter.api.assertThrows<Exception> {
            logCsvParserForProject.parseLine(sampleLine)
        }
    }

    @Test
    fun `should throw exception if both project current entity and previous entity of log are null `(){
        // Given
        every {
            projectCsvParser.parseLine(match {
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
            logCsvParserForProject.parseLine(sampleLine)
        }
    }


}