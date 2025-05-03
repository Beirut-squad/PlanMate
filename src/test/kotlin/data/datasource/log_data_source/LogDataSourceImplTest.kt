package data.datasource.log_data_source

import com.google.common.truth.Truth.assertThat
import creator_helper.projectLog
import creator_helper.projectLogsByProjectId
import creator_helper.projectLogsForAllUsers
import creator_helper.taskLog
import creator_helper.taskLogsByTaskId
import creator_helper.taskLogsForAllUsers
import creator_helper.testProjectId
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
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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

    @Test
    fun `should return project logs by project id when logs found`() {
        every { csvProjectLogReader.read(any()) } returns projectLogsByProjectId

        val result = logDataSourceImpl.getProjectLogs(testProjectId)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(projectLogsByProjectId)
    }

    @Test
    fun `should return failure when no project logs found for given id`() {
        val notExistedId = UUID.randomUUID()
        every { csvProjectLogReader.read(any()) } returns projectLogsByProjectId

        val result = logDataSourceImpl.getProjectLogs(notExistedId)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull() is NoProjectLogsFoundException)
    }

    @Test
    fun `should return task logs by task id when logs found`() {
        every { csvTaskLogReader.read(any()) } returns taskLogsByTaskId

        val result = logDataSourceImpl.getTaskLogs(testTaskId)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(taskLogsByTaskId)
    }

    @Test
    fun `should return failure when no task logs found for given id`() {
        val notExistedId = UUID.randomUUID()
        every { csvTaskLogReader.read(any()) } returns taskLogsByTaskId

        val result = logDataSourceImpl.getTaskLogs(notExistedId)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull() is NoTaskLogsFoundException)
    }

    @Test
    fun `should save project log successfully`() {
        every { csvProjectLogReader.read(any()) } returns projectLogsForAllUsers
        every { csvProjectLogWriter.writeToFile(any(), any()) } returns Result.success(Unit)

        val result = logDataSourceImpl.saveProjectLog(projectLog)

        assertThat(result.isSuccess).isTrue()
        verify { csvProjectLogWriter.writeToFile(any(), any()) }
    }

    @Test
    fun `should return failure when writing project log fails`() {
        every { csvProjectLogReader.read(any()) } returns projectLogsForAllUsers
        every { csvProjectLogWriter.writeToFile(any(), any()) } returns Result.failure(Exception())

        val result = logDataSourceImpl.saveProjectLog(projectLog)

        assertThat(result.isFailure).isTrue()
    }


    @Test
    fun `should save task log successfully`() {
        every { csvTaskLogReader.read(any()) } returns taskLogsForAllUsers
        every { csvTaskLogWriter.writeToFile(any(), any()) } returns Result.success(Unit)

        val result = logDataSourceImpl.saveTaskLog(taskLog)

        assertThat(result.isSuccess).isTrue()
        verify { csvTaskLogWriter.writeToFile(any(), any()) }
    }

    @Test
    fun `should return failure when writing task log fails`() {
        every { csvTaskLogReader.read(any()) } returns taskLogsForAllUsers
        every { csvTaskLogWriter.writeToFile(any(), any()) } returns Result.failure(Exception())

        val result = logDataSourceImpl.saveTaskLog(taskLog)

        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `should return success with all project logs`() {
        every { csvProjectLogReader.read(any()) } returns projectLogsForAllUsers

        val result = logDataSourceImpl.getAllProjectLogs()

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(projectLogsForAllUsers)
    }

    @Test
    fun `should return failure when no project logs found`() {
        every { csvProjectLogReader.read(any()) } returns emptyList<ProjectLog>()

        val result = logDataSourceImpl.getAllProjectLogs()

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull() is NoProjectLogsFoundException)
    }

    @Test
    fun `should return success with all task logs`() {
        every { csvTaskLogReader.read(any()) } returns taskLogsForAllUsers

        val result = logDataSourceImpl.getAllTaskLogs()

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(taskLogsForAllUsers)
    }

    @Test
    fun `should return failure when no task logs found`() {
        every { csvTaskLogReader.read(any()) } returns emptyList<TaskLog>()

        val result = logDataSourceImpl.getAllTaskLogs()

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull() is NoTaskLogsFoundException)
    }
}