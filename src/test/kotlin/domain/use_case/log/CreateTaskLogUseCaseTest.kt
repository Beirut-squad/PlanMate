package domain.use_case.log

import com.google.common.truth.Truth.assertThat
import creator_helper.taskLog
import creator_helper.testUserId
import domain.model.TaskLog
import domain.repository.LogRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import org.junit.jupiter.api.assertThrows
import ui.common.exception.NullTasksComparisonException

class CreateTaskLogUseCaseTest {

    private val logRepository: LogRepository = mockk(relaxed = true)
    private lateinit var createTaskLogUseCase: CreateTaskLogUseCase

    @BeforeEach
    fun setUp() {
        createTaskLogUseCase = CreateTaskLogUseCase(logRepository)
    }

    @Test
    fun `createTaskLog should throw NullTasksComparisonException when previous and current task are null`() = runTest {
        // Given
        val previousProject = null
        val currentProject = null

        // When & Then
        assertThrows<NullTasksComparisonException> {
            createTaskLogUseCase.createTaskLog(testUserId, previousProject, currentProject)
        }
    }

    @Test
    fun `createTaskLog should create task log when both previous and current task are not null`() = runTest {
        // Given
        var capturedLog: TaskLog? = null
        coEvery { logRepository.saveTaskLog(any()) } answers {
            capturedLog = firstArg()
        }

        // When
        createTaskLogUseCase.createTaskLog(testUserId, taskLog.previousEntity, taskLog.currentEntity)

        // Then
        coVerify(exactly = 1) { logRepository.saveTaskLog(any()) }
        assertThat(capturedLog).isNotNull()
        with(capturedLog!!) {
            assertThat(this.userId).isEqualTo(testUserId)
            assertThat(this.entityId).isEqualTo(taskLog.entityId)
            assertThat(this.previousEntity).isEqualTo(taskLog.previousEntity)
            assertThat(this.currentEntity).isEqualTo(taskLog.currentEntity)
        }
    }

    @Test
    fun `createTaskLog should create task log when previous task is null`() = runTest {
        // Given
        val currentTask = taskLog.currentEntity
        var capturedLog: TaskLog? = null
        coEvery { logRepository.saveTaskLog(any()) } answers {
            capturedLog = firstArg()
        }

        // When
        createTaskLogUseCase.createTaskLog(testUserId, null, currentTask)

        // Then
        coVerify(exactly = 1) { logRepository.saveTaskLog(any()) }
        assertThat(capturedLog).isNotNull()
        with(capturedLog!!) {
            assertThat(this.userId).isEqualTo(testUserId)
            assertThat(this.entityId).isEqualTo(currentTask?.id)
            assertThat(this.previousEntity).isNull()
            assertThat(this.currentEntity).isEqualTo(currentTask)
        }
    }

    @Test
    fun `createTaskLog should create task log when current task is null`() = runTest {
        // Given
        val previousEntity = taskLog.previousEntity
        var capturedLog: TaskLog? = null
        coEvery { logRepository.saveTaskLog(any()) } answers {
            capturedLog = firstArg()
        }

        // When
        createTaskLogUseCase.createTaskLog(testUserId, previousEntity, null)

        //Then
        coVerify(exactly = 1) { logRepository.saveTaskLog(any()) }
        assertThat(capturedLog).isNotNull()
        with(capturedLog!!) {
            assertThat(this.userId).isEqualTo(testUserId)
            assertThat(this.entityId).isEqualTo(previousEntity?.id)
            assertThat(this.previousEntity).isEqualTo(previousEntity)
            assertThat(this.currentEntity).isNull()
        }
    }

    @Test
    fun `createTaskLog should throw exception when saveTaskLog returns failure`() = runTest {
        // Given
        val previousTask = null
        val currentTask = null
        coEvery { logRepository.saveTaskLog(any()) } throws Exception("Error saving log")

        // When & Then
        assertThrows<Exception> {
            createTaskLogUseCase.createTaskLog(testUserId, previousTask, currentTask)
        }
    }
}