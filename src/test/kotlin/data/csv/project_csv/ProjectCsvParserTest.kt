package data.csv.project_csv

import com.google.common.truth.Truth.assertThat
import creator_helper.createProjectHelper
import creator_helper.createStateHelper
import creator_helper.createTaskHelper
import io.mockk.every
import io.mockk.mockk
import org.example.data.csv.state_csv.StateCsvParser
import org.example.data.csv.task_csv_parser.TaskCsvParser
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.util.*

class ProjectCsvParserTest{

    private lateinit var stateCsvParser: StateCsvParser
    private lateinit var projectCsvParser: ProjectCsvParser

    @BeforeEach
    fun setup(){
        stateCsvParser  = mockk(relaxed = true)
        projectCsvParser = ProjectCsvParser(stateCsvParser)
    }

    @Test
    fun `parseLine should return null when the string is empty `(){
        // When
        val result = projectCsvParser.parseLine("[      ]")

        // Then
        assertThat(result).isNull()
    }

    @Test
    fun `parseLine should return null when  the string is empty `(){
        // When
        val result = projectCsvParser.parseLine("  ")

        // Then
        assertThat(result).isNull()
    }

    @Test
    fun `parseLine should return a Project object when the string is not empty `(){
        // When
        val taskString = "[d1234567-89ab-cdef-0123-456789abcdef, Project Name,Project Description, 5481551e-2b45-49a0-b5fc-123456789012 ,2024-04-01T12:00:00,2024-04-02T12:00:00 ,[[5481551e-2b45-49a0-b5fc-123456789012 , name] , [5481551e-2b45-49a0-b5fc-123456789012 , name]]]"
        val statesString = "[[5481551e-2b45-49a0-b5fc-123456789012 , name] , [5481551e-2b45-49a0-b5fc-123456789012 , name]]"
        val states = projectCsvParser.parseMultiStates(statesString)
        val result = projectCsvParser.parseLine(taskString)


        // then

        assertThat(result).isEqualTo(
            createProjectHelper(
                UUID.fromString("d1234567-89ab-cdef-0123-456789abcdef"),
                "Project Name",
                "Project Description",
                UUID.fromString("5481551e-2b45-49a0-b5fc-123456789012"),
                LocalDateTime.parse("2024-04-01T12:00:00"),
                LocalDateTime.parse("2024-04-02T12:00:00"),
                states!!
            )
        )
    }

    @Test
    fun ` parseFile should return empty list if there is no csv lines`() {

        // When
        val result = projectCsvParser.parseFile(emptyList())

        // Then
        assertThat(result).isEmpty()
    }



    @Test
    fun `should return a list of Projects when csv contains lines `(){

        // When
        val taskString1 = "[d1234567-89ab-cdef-0123-456789abcdef, Project Name,Project Description, 5481551e-2b45-49a0-b5fc-123456789012 ,2024-04-01T12:00:00,2024-04-02T12:00:00 ,[[5481551e-2b45-49a0-b5fc-123456789012 , name] , [5481551e-2b45-49a0-b5fc-123456789012 , name]]]"
        val taskString2 = "[d1234567-89ab-cdef-0123-456789abcdef, Project Name,Project Description, 5481551e-2b45-49a0-b5fc-123456789012 ,2024-04-01T12:00:00,2024-04-02T12:00:00 ,[[5481551e-2b45-49a0-b5fc-123456789012 , name] , [5481551e-2b45-49a0-b5fc-123456789012 , name]]]"
        val taskString3 = "[   ]"
        val taskString4 = "[d1234567-89ab-cdef-0123-456789abcdef, Project Name,Project Description, 5481551e-2b45-49a0-b5fc-123456789012 ,2024-04-01T12:00:00,2024-04-02T12:00:00 ,[[5481551e-2b45-49a0-b5fc-123456789012 , name] , [5481551e-2b45-49a0-b5fc-123456789012 , name]]]"
        val csvLines = listOf(taskString1,taskString2,taskString3,taskString4)

        val statesString = "[[5481551e-2b45-49a0-b5fc-123456789012 , name] , [5481551e-2b45-49a0-b5fc-123456789012 , name]]"
        val states = projectCsvParser.parseMultiStates(statesString)

        val result = projectCsvParser.parseFile(csvLines)

        // Then
        assertThat(result).isEqualTo(
            listOf(
                createProjectHelper(
                    UUID.fromString("d1234567-89ab-cdef-0123-456789abcdef"),
                    "Project Name",
                    "Project Description",
                    UUID.fromString("5481551e-2b45-49a0-b5fc-123456789012"),
                    LocalDateTime.parse("2024-04-01T12:00:00"),
                    LocalDateTime.parse("2024-04-02T12:00:00"),
                    states!!
                ),
                createProjectHelper(
                    UUID.fromString("d1234567-89ab-cdef-0123-456789abcdef"),
                    "Project Name",
                    "Project Description",
                    UUID.fromString("5481551e-2b45-49a0-b5fc-123456789012"),
                    LocalDateTime.parse("2024-04-01T12:00:00"),
                    LocalDateTime.parse("2024-04-02T12:00:00"),
                    states
                ),
                createProjectHelper(
                    UUID.fromString("d1234567-89ab-cdef-0123-456789abcdef"),
                    "Project Name",
                    "Project Description",
                    UUID.fromString("5481551e-2b45-49a0-b5fc-123456789012"),
                    LocalDateTime.parse("2024-04-01T12:00:00"),
                    LocalDateTime.parse("2024-04-02T12:00:00"),
                    states
                )
            )
        )
    }


//    @Test
//    fun `should throw exception if the state of project is null `(){
//        // Given
//        every {
//            stateCsvParser.parseLine(match {
//                val cleaned = it.trim()
//                cleaned.isEmpty() || cleaned.matches(Regex("""\[[ \t]*]"""))
//            })
//        } returns null
//
//        // When
//        val taskString = "[d1234567-89ab-cdef-0123-456789abcdef, Project Name,Project Description, 5481551e-2b45-49a0-b5fc-123456789012 ,2024-04-01T12:00:00,2024-04-02T12:00:00 ,[]]"
//
//        // then
//        assertThrows<Exception> {
//            projectCsvParser.parseLine(taskString)
//        }
//    }

}