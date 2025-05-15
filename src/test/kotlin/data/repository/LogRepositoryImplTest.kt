package data.repository

import creator_helper.createProjectLogHelper
import creator_helper.createTaskLogHelper
import data.datasource.LogDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class LogRepositoryImplTest {
    private val logDataSource: LogDataSource = mockk()
    private lateinit var logRepository: LogRepositoryImpl
    private val testProjectId = UUID.randomUUID()
    private val testTaskId = UUID.randomUUID()
    private val testProjectLog = createProjectLogHelper()
    private val testTaskLog = createTaskLogHelper()

    @BeforeEach
    fun setup() {
        logRepository = LogRepositoryImpl(logDataSource)
    }

    @Test
    fun `getProjectLogs should return list of project logs`() = runTest {
        val logs = listOf(testProjectLog)
        coEvery { logDataSource.getProjectLogs(any()) } returns logs
        val result = logRepository.getProjectLogs(testProjectId)
        assertEquals(logs, result)
        coVerify { logDataSource.getProjectLogs(testProjectId) }
    }

    @Test
    fun `getTaskLogs should return list of task logs`() = runTest {
        val logs = listOf(testTaskLog)
        coEvery { logDataSource.getTaskLogs(any()) } returns logs
        val result = logRepository.getTaskLogs(testTaskId)
        assertEquals(logs, result)
        coVerify { logDataSource.getTaskLogs(testTaskId) }
    }

    @Test
    fun `saveProjectLog should call datasource`() = runTest {
        coEvery { logDataSource.saveProjectLog(any()) } returns Unit
        logRepository.saveProjectLog(testProjectLog)
        coVerify { logDataSource.saveProjectLog(testProjectLog) }
    }

    @Test
    fun `saveTaskLog should call datasource`() = runTest {
        coEvery { logDataSource.saveTaskLog(any()) } returns Unit
        logRepository.saveTaskLog(testTaskLog)
        coVerify { logDataSource.saveTaskLog(testTaskLog) }
    }

    @Test
    fun `getAllProjectLogs should return list of all project logs`() = runTest {
        val logs = listOf(testProjectLog)
        coEvery { logDataSource.getAllProjectLogs() } returns logs
        val result = logRepository.getAllProjectLogs()
        assertEquals(logs, result)
        coVerify { logDataSource.getAllProjectLogs() }
    }

    @Test
    fun `getAllTaskLogs should return list of all task logs`() = runTest {
        val logs = listOf(testTaskLog)
        coEvery { logDataSource.getAllTaskLogs() } returns logs
        val result = logRepository.getAllTaskLogs()
        assertEquals(logs, result)
        coVerify { logDataSource.getAllTaskLogs() }
    }
}