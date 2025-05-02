package data.repositories.log_repository

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
import io.mockk.mockk
import org.example.data.datasource.log_data_source.LogDataSource
import org.example.data.repositories.log_repository.LogRepositoryImpl
import org.example.logic.repositories.log_repository.LogRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LogRepositoryImplTest {
    private  val logDataSource: LogDataSource= mockk(relaxed = true)
    private lateinit var logRepositoryImpl:  LogRepository

    @BeforeEach
    fun setup() {
        logRepositoryImpl = LogRepositoryImpl(logDataSource)
    }

    @Test
    fun `should return project logs for given project id when logs exists`() {
        every { logDataSource.getProjectLogs(testProjectId) } returns Result.success(projectLogsByProjectId)
        val result = logRepositoryImpl.getProjectLogs(testProjectId)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(projectLogsByProjectId)
    }

    @Test
    fun `should return failure when no logs for a given project`() {
        val exception = Exception("No logs found")
        every { logDataSource.getProjectLogs(testProjectId) } returns Result.failure(exception)
        val result = logRepositoryImpl.getProjectLogs(testProjectId)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
    }

    @Test
    fun `should return task logs for given task id when logs exists`() {
        every { logDataSource.getTaskLogs(testTaskId) } returns Result.success(taskLogsByTaskId)
        val result = logRepositoryImpl.getTaskLogs(testTaskId)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(taskLogsByTaskId)
    }

    @Test
    fun `should return failure when no logs for a given task`() {
        val exception = Exception("No logs found")
        every { logDataSource.getTaskLogs(testTaskId) } returns Result.failure(exception)
        val result = logRepositoryImpl.getTaskLogs(testTaskId)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
    }

    @Test
    fun `should return success when project log is saved successfully`() {
        every{logDataSource.saveProjectLog(projectLog)} returns Result.success(Unit)

        val result = logRepositoryImpl.saveProjectLog(projectLog)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `should return failure when a project log fail to be saved`() {
        val exception = Exception("Error saving logs")
        every{logDataSource.saveProjectLog(projectLog)} returns Result.failure(exception)

        val result = logRepositoryImpl.saveProjectLog(projectLog)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
    }

    @Test
    fun `should return success when task log is saved successfully`() {
        every{logDataSource.saveTaskLog(taskLog)} returns Result.success(Unit)

        val result = logRepositoryImpl.saveTaskLog(taskLog)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `should return failure when a task log fail to be saved`() {
        val exception = Exception("Error saving logs")
        every{logDataSource.saveTaskLog(taskLog)} returns Result.failure(exception)

        val result = logRepositoryImpl.saveTaskLog(taskLog)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
    }

    @Test
    fun `should return all project logs when logs exist`() {
        every { logDataSource.getAllProjectLogs() } returns Result.success(projectLogsForAllUsers)
        val result = logRepositoryImpl.getAllProjectLogs()

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(projectLogsForAllUsers)
    }

    @Test
    fun `should return failure when no project logs exist`() {
        val exception = Exception("No logs found")
        every { logDataSource.getAllProjectLogs() } returns Result.failure(exception)
        val result = logRepositoryImpl.getAllProjectLogs()

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
    }

    @Test
    fun `should return all task logs when logs exist`() {
        every { logDataSource.getAllTaskLogs() } returns Result.success(taskLogsForAllUsers)
        val result = logRepositoryImpl.getAllTaskLogs()

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(taskLogsForAllUsers)
    }

    @Test
    fun `should return failure when no task logs exist`() {
        val exception = Exception("No logs found")
        every { logDataSource.getAllTaskLogs() } returns Result.failure(exception)
        val result = logRepositoryImpl.getAllTaskLogs()

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
    }

}