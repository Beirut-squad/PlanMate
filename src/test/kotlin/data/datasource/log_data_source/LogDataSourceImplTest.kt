package data.datasource.log_data_source

import com.google.common.truth.Truth.assertThat
import creator_helper.projectLog
import creator_helper.projectLogsForAllUsers
import creator_helper.taskLog
import creator_helper.taskLogsByTaskId
import creator_helper.taskLogsForAllUsers
import creator_helper.testTaskId
import io.mockk.every
import io.mockk.verify
import io.mockk.mockk
import org.example.data.csv.CsvReader
import org.example.data.csv.CsvWriter
import org.example.data.datasource.log_data_source.LogDataSourceImpl
import org.example.logic.exceptions.log_exceptions.NoProjectLogsFoundException
import org.example.logic.exceptions.log_exceptions.NoTaskLogsFoundException
import org.example.models.ProjectLog
import org.example.models.TaskLog
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.IOException
import java.util.UUID

class LogDataSourceImplTest {
    private val csvProjectLogReader: CsvReader<ProjectLog> = mockk(relaxed = true)
    private val csvProjectLogWriter: CsvWriter<ProjectLog> = mockk(relaxed = true)
    private val csvTaskLogReader: CsvReader<TaskLog> = mockk(relaxed = true)
    private val csvTaskLogWriter: CsvWriter<TaskLog> = mockk(relaxed = true)
    private lateinit var logDataSourceImpl: LogDataSourceImpl

    @BeforeEach
    fun setUp() {
        logDataSourceImpl =
            LogDataSourceImpl(csvProjectLogReader, csvProjectLogWriter, csvTaskLogReader, csvTaskLogWriter)
    }

    //region getTaskLogs
    @Test
    fun `getTaskLogs should return task logs by task id when logs found`() {
        // Given
        every { csvTaskLogReader.read(any()) } returns taskLogsByTaskId

        // When
        val result = logDataSourceImpl.getTaskLogs(testTaskId)

        // Then
        assertThat(result.size).isEqualTo(taskLogsByTaskId.size)
    }

    @Test
    fun `getTaskLogs should return failure when no task logs found for given id`() {
        // Given
        val notExistedId = UUID.randomUUID()
        every { csvTaskLogReader.read(any()) } returns taskLogsByTaskId

        // When & Then
        assertThrows<NoTaskLogsFoundException> {
            logDataSourceImpl.getTaskLogs(notExistedId)
        }
    }
    //endregion

    //region saveProjectLog
    @Test
    fun `saveProjectLog should save project log successfully`() {
        // Given
        every { csvProjectLogReader.read(any()) } returns projectLogsForAllUsers
        val expectedLogs = projectLogsForAllUsers + projectLog
        var capturedLogs: List<ProjectLog>? = null
        every { csvProjectLogWriter.writeToFile(any(), any()) } answers {
            capturedLogs = firstArg()
        }

        // When
        logDataSourceImpl.saveProjectLog(projectLog)

        // Then
        verify(exactly = 1) { csvProjectLogReader.read(any()) }
        verify(exactly = 1) { csvProjectLogWriter.writeToFile(any(), any()) }
        assertNotNull(capturedLogs)
        assertEquals(expectedLogs.size, capturedLogs?.size)
        assertTrue(capturedLogs!!.containsAll(expectedLogs))
    }

    @Test
    fun `saveProjectLog should throw RuntimeException when reading project log fails`() {
        // Given
        every { csvProjectLogReader.read(any()) } throws RuntimeException("Read error")

        // When/Then
        assertThrows<RuntimeException> {
            logDataSourceImpl.saveProjectLog(projectLog)
        }
    }

    @Test
    fun `saveProjectLog should throw IOException when writing project log fails`() {
        // Given
        every { csvProjectLogReader.read(any()) } returns projectLogsForAllUsers
        every { csvProjectLogWriter.writeToFile(any(), any()) } throws IOException("Write error")

        // When & Then
        assertThrows<IOException> {
            logDataSourceImpl.saveProjectLog(projectLog)
        }
    }
    //endregion

    //region saveTaskLog
    @Test
    fun `saveTaskLog should save task log successfully`() {
        // Given
        every { csvTaskLogReader.read(any()) } returns taskLogsForAllUsers
        val expectedLogs = taskLogsForAllUsers + taskLog
        var capturedLogs: List<TaskLog>? = null
        every { csvTaskLogWriter.writeToFile(any(), any()) } answers {
            capturedLogs = firstArg()
        }

        // When
        logDataSourceImpl.saveTaskLog(taskLog)

        // Then
        verify(exactly = 1) { csvTaskLogReader.read(any()) }
        verify(exactly = 1) { csvTaskLogWriter.writeToFile(any(), any()) }
        assertNotNull(capturedLogs)
        assertEquals(expectedLogs.size, capturedLogs?.size)
        assertTrue(capturedLogs!!.containsAll(expectedLogs))
    }

    @Test
    fun `saveTaskLog should throw RuntimeException when reading task log fails`() {
        // Given
        every { csvTaskLogReader.read(any()) } throws RuntimeException("Read error")

        // When/Then
        assertThrows<RuntimeException> {
            logDataSourceImpl.saveTaskLog(taskLog)
        }
    }

    @Test
    fun `saveTaskLog should throw IOException when writing task log fails`() {
        // Given
        every { csvTaskLogReader.read(any()) } returns taskLogsForAllUsers
        every { csvTaskLogWriter.writeToFile(any(), any()) } throws IOException("Write error")

        // When & Then
        assertThrows<IOException> {
            logDataSourceImpl.saveTaskLog(taskLog)
        }
    }
    //endregion

    //region getAllProjectLogs
    @Test
    fun `getAllProjectLogs should return success with all project logs`() {
        // Given
        every { csvProjectLogReader.read(any()) } returns projectLogsForAllUsers

        // When
        val result = logDataSourceImpl.getAllProjectLogs()

        // Then
        assertThat(result).isEqualTo(projectLogsForAllUsers)
    }

    @Test
    fun `getAllProjectLogs should throw NoProjectLogsFoundException when no project logs found`() {
        // Given
        every { csvProjectLogReader.read(any()) } returns emptyList()

        // When & Then
        assertThrows<NoProjectLogsFoundException> {
            logDataSourceImpl.getAllProjectLogs()
        }
    }
    //endregion

    //region saveTaskLog
    @Test
    fun `getAllTaskLogs should return success with all task logs`() {
        // Given
        every { csvTaskLogReader.read(any()) } returns taskLogsForAllUsers

        // When
        val result = logDataSourceImpl.getAllTaskLogs()

        // Then
        assertThat(result).isEqualTo(taskLogsForAllUsers)
    }

    @Test
    fun `getAllTaskLogs should throw NoTaskLogsFoundException when no task logs found`() {
        // Given
        every { csvTaskLogReader.read(any()) } returns emptyList<TaskLog>()

        // When & Then
        assertThrows<NoTaskLogsFoundException> {
            logDataSourceImpl.getAllTaskLogs()
        }
    }
    //endregion
}