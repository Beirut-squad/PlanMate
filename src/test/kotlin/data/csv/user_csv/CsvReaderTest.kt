package data.csv.user_csv

import CsvParser
import com.google.common.truth.Truth.assertThat
import creator_helper.createProjectHelper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.data.csv.CsvReader
import kotlin.test.Test
import org.example.models.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.io.File
import java.time.LocalDateTime
import java.util.*

class CsvReaderTest{


    private lateinit var stateCsvParser: CsvParser<State>
    private lateinit var projectCsvParser: CsvParser<Project>
    private lateinit var taskCsvParser: CsvParser<Task>
    private lateinit var logCsvParserForProject: CsvParser<ProjectLog>
    private lateinit var logCsvParserForTask: CsvParser<TaskLog>
    private lateinit var csvReader: CsvReader<Project>


    @BeforeEach
    fun setup() {
        stateCsvParser = mockk<CsvParser<State>>(relaxed = true)
        projectCsvParser = mockk<CsvParser<Project>>(relaxed = true)
        taskCsvParser = mockk<CsvParser<Task>>(relaxed = true)
        logCsvParserForProject = mockk<CsvParser<ProjectLog>>(relaxed = true)
        logCsvParserForTask = mockk<CsvParser<TaskLog>>(relaxed = true)


        csvReader = CsvReader(projectCsvParser)
    }

    @Test
    fun `should throw exception if the file is not found in file names `() {
        // When && Then
        assertThrows<Exception>{
            csvReader.read("non_existing_file.csv")
        }
    }

    @Test
    fun `should throw exception if the file is not found in the required path`() {
        val expectedFile = "expected_File.csv"
        // When && Then
        assertThrows<Exception>{
            csvReader.read(expectedFile)
        }
    }

    @Test
    fun `should call parser if the file exists `(){
        // When
        csvReader.read("projects.csv")

        // Then
        verify { projectCsvParser.parseFile(any()) }
    }
}
