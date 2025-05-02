package data.csv.log_for_project_csv

import creator_helper.createProjectLogHelper
import org.example.data.csv.log_csv_parser.LogCsvWriterForProject
import org.example.models.ProjectLog
import org.junit.jupiter.api.Assertions.*
import java.io.File
import java.io.FileNotFoundException
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test

class LogCsvWriterForProjectTest{
     private lateinit var logCsvWriterForProject: LogCsvWriterForProject
     private lateinit var filePath: String

     @BeforeTest
     fun setUp(){
         logCsvWriterForProject = LogCsvWriterForProject()
         filePath = "test_projectLog.csv"
     }

    @Test
    fun `given list of projects when writeToFile is called then file should be created`(){
        //Given
        val listOfProjectLogs = listOf(createProjectLogHelper(),createProjectLogHelper())
        val file = File(filePath)

        //when
        logCsvWriterForProject.writeToFile(listOfProjectLogs,filePath)

        //then

        assertTrue(file.exists())
        file.delete()
    }

    @Test
    fun `given empty list of projects when writeToFile is called then file shoud contain only header`(){
    //Given
    val listOfProjectLogs = emptyList<ProjectLog>()
    val file = File(filePath)
    assertTrue(file.length() == 0L)
    val content = file.readText()
    //when
    logCsvWriterForProject.writeToFile(listOfProjectLogs,filePath)

    //then

    assertTrue(file.exists())
    assertTrue(content.contains("id,userId,entityId,previousEntity,currentEntity,createdAt"))
    assertTrue(content.lines().size == 2)
    assertTrue(content.lines()[1].isBlank())
    file.delete()
}

    @Test
    fun `given invalid file name when writeToFile is called then return failure`() {
        //Given
        val invalidFilePath = "invalid|file.csv"
        val listOfProjectLogs = listOf(createProjectLogHelper(),createProjectLogHelper())

        //when
        val result = logCsvWriterForProject.writeToFile(listOfProjectLogs,invalidFilePath)

        //then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `given projectLog with invalid ID when isValidProjectLog is called then should return false`() {
        //Given
        val projectLog = createProjectLogHelper(id = UUID(0,0))

        //When
        val result = logCsvWriterForProject.isValidProjectLog(projectLog)

        assertFalse(result)
    }

    @Test
    fun `given projectLog with invalid userId when isValidProjectLog is called then should return false`() {
        //Given
        val projectLog = createProjectLogHelper(userId = UUID(0,0))

        //When
        val result = logCsvWriterForProject.isValidProjectLog(projectLog)

        assertFalse(result)
    }

    @Test
    fun `given projectLog with invalid entityId when isValidProjectLog is called then should return false`() {
        //Given
        val projectLog = createProjectLogHelper(entityId = UUID(0,0))

        //When
        val result = logCsvWriterForProject.isValidProjectLog(projectLog)

        assertFalse(result)
    }

    @Test
    fun `given projectLog with empty previousEntity when isValidProjectLog is called then should return true`() {
        //Given
        val projectLog = createProjectLogHelper(previousEntity = null)

        //When
        val result = logCsvWriterForProject.isValidProjectLog(projectLog)

        assertTrue(result)
    }

    @Test
    fun `given projectLog with empty currentEntity when isValidProjectLog is called then should return true`() {
        //Given
        val projectLog = createProjectLogHelper(currentEntity =null)

        //When
        val result = logCsvWriterForProject.isValidProjectLog(projectLog)

        assertTrue(result)
    }


}

