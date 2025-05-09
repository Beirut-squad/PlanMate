package data.repositories.log_repository

import com.google.common.truth.Truth.assertThat
import creator_helper.*
import io.mockk.*
import org.example.data.datasource.log_data_source.LogDataSource
import org.example.data.repositories.log_repository.LogRepositoryImpl
import org.example.logic.exceptions.log_exceptions.NoTaskLogsFoundException
import org.example.logic.repositories.log_repository.LogRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class LogRepositoryImplTest {
    private val logDataSource: LogDataSource = mockk(relaxed = true)
    private lateinit var logRepositoryImpl: LogRepository

    @BeforeEach
    fun setup() {
        logRepositoryImpl = LogRepositoryImpl(logDataSource)
    }

    //region getTaskLogs
    @Test
    fun `getTaskLogs should return task logs for given task id when logs exists`() {
        // Given
        every { logDataSource.getTaskLogs(testTaskId) } returns taskLogsByTaskId

        // When
        val result = logRepositoryImpl.getTaskLogs(testTaskId)

        // Then
        assertThat(result).isEqualTo(taskLogsByTaskId)
    }

    @Test
    fun `getTaskLogs should throws NoTaskLogsFoundException when no logs for a given task`() {
        // Given
        val exception = Exception("No logs found")
        every { logDataSource.getTaskLogs(testTaskId) } throws NoTaskLogsFoundException()

        // When & Then
        assertThrows<NoTaskLogsFoundException> {
            logRepositoryImpl.getTaskLogs(testTaskId)
        }
    }
    //endregion

    //region saveProjectLog
    @Test
    fun `saveProjectLog should return success when project log is saved successfully`() {
        // Given
        every { logDataSource.saveProjectLog(projectLog) } just Runs

        // When
        logRepositoryImpl.saveProjectLog(projectLog)

        // Then
        verify(exactly = 1) {
            logRepositoryImpl.saveProjectLog(projectLog)
        }
    }

    @Test
    fun `saveProjectLog should throw exception when a project log fail to be saved`() {
        // Given
        every { logDataSource.saveProjectLog(projectLog) } throws Exception("Error saving logs")

        // When & Then
        assertThrows<Exception> {
            logRepositoryImpl.saveProjectLog(projectLog)
        }
    }
    //endregion

    //region saveTaskLog
    @Test
    fun `saveTaskLog should return success when task log is saved successfully`() {
        // Given
        every { logDataSource.saveTaskLog(taskLog) } just Runs

        // When
        logRepositoryImpl.saveTaskLog(taskLog)

        // Then
        verify(exactly = 1) {
            logDataSource.saveTaskLog(taskLog)
        }
    }

    @Test
    fun `saveTaskLog should throw exception when a task log fail to be saved`() {
        // Given
        every { logDataSource.saveTaskLog(taskLog) } throws Exception("Error saving logs")

        // When & Then
        assertThrows<Exception> {
            logRepositoryImpl.saveTaskLog(taskLog)
        }
    }
    //endregion

    //region getAllProjectLogs
    @Test
    fun `getAllProjectLogs should return all project logs when logs exist`() {
        // Given
        every { logDataSource.getAllProjectLogs() } returns projectLogsForAllUsers

        // When
        val result = logRepositoryImpl.getAllProjectLogs()

        // Then
        assertThat(result).isEqualTo(projectLogsForAllUsers)
    }

    @Test
    fun `getAllProjectLogs should throw exception when no project logs exist`() {
        // Given
        every { logDataSource.getAllProjectLogs() } throws Exception("No logs found")

        // When & Then
        assertThrows<Exception> {
            logRepositoryImpl.getAllProjectLogs()
        }
    }
    //endregion

    //region getAllTaskLogs
    @Test
    fun `getAllTaskLogs should return all task logs when logs exist`() {
        // Given
        every { logDataSource.getAllTaskLogs() } returns taskLogsForAllUsers

        // When
        val result = logRepositoryImpl.getAllTaskLogs()

        // Then
        assertThat(result).isEqualTo(taskLogsForAllUsers)
    }

    @Test
    fun `getAllTaskLogs should return throw exception when no task logs exist`() {
        // Given
        every { logDataSource.getAllTaskLogs() } throws Exception("No logs found")

        // When & Then
        assertThrows<Exception> {
            logRepositoryImpl.getAllTaskLogs()
        }
    }
    //endregion
}