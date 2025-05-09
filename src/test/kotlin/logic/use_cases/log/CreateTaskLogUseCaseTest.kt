package logic.use_cases.log

import com.google.common.truth.Truth.assertThat
import creator_helper.task1
import creator_helper.taskLog
import creator_helper.testUserId
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.logic.exceptions.log_exceptions.InvalidCreateTaskLogException
import org.example.logic.repositories.log_repository.LogRepository
import org.example.models.TaskLog
import org.junit.jupiter.api.BeforeEach
import java.util.UUID
import kotlin.test.Test
import org.junit.jupiter.api.assertThrows

class CreateTaskLogUseCaseTest {

    private val logRepository: LogRepository = mockk(relaxed = true)
    private lateinit var createTaskLogUseCase: CreateTaskLogUseCase
    private val userId = UUID.fromString("10000000-0000-0000-0000-000000000000")

    @BeforeEach
    fun setUp() {
        createTaskLogUseCase = CreateTaskLogUseCase(logRepository)
    }

    @Test
    fun `createTaskLog should return throw InvalidCreateTaskLogException when previous and current task are null`() {
        // Given
        val previousProject = null
        val currentProject = null

        // When & Then
        assertThrows<InvalidCreateTaskLogException> {
            createTaskLogUseCase.createTaskLog(testUserId, previousProject, currentProject)
        }
    }

    @Test
    fun `createTaskLog should create task log and return success when both previous and current task are not null`() {
        // Given
        var capturedLog: TaskLog? = null
        every { logRepository.saveTaskLog(any()) } answers {
            capturedLog = firstArg()
        }

        // When
        createTaskLogUseCase.createTaskLog(userId, taskLog.previousEntity, taskLog.currentEntity)

        // Then
        verify(exactly = 1) { logRepository.saveTaskLog(any()) }
        assertThat(capturedLog).isNotNull()
        with(capturedLog!!) {
            assertThat(this.userId).isEqualTo(userId)
            assertThat(this.entityId).isEqualTo(taskLog.entityId)
            assertThat(this.previousEntity).isEqualTo(taskLog.previousEntity)
            assertThat(this.currentEntity).isEqualTo(taskLog.currentEntity)
        }
    }

    @Test
    fun `createTaskLog should create task log and return success when previous task is null`() {
        // Given
        var capturedLog: TaskLog? = null
        every { logRepository.saveTaskLog(any()) } answers {
            capturedLog = firstArg()
        }

        // When
        createTaskLogUseCase.createTaskLog(userId, null, task1)

        // Then
        verify(exactly = 1) { logRepository.saveTaskLog(any()) }
        assertThat(capturedLog).isNotNull()
        with(capturedLog!!) {
            assertThat(this.userId).isEqualTo(userId)
            assertThat(this.entityId).isEqualTo(task1.id)
            assertThat(this.previousEntity).isNull()
            assertThat(this.currentEntity).isEqualTo(task1)
        }
    }

    @Test
    fun `createTaskLog should create task log and return success when current task is null`() {
        // Given
        var capturedLog: TaskLog? = null
        every { logRepository.saveTaskLog(any()) } answers {
            capturedLog = firstArg()
        }

        // When
        createTaskLogUseCase.createTaskLog(userId, task1, null)

        //Then
        verify(exactly = 1) { logRepository.saveTaskLog(any()) }
        assertThat(capturedLog).isNotNull()
        with(capturedLog!!) {
            assertThat(this.userId).isEqualTo(userId)
            assertThat(this.entityId).isEqualTo(task1.id)
            assertThat(this.previousEntity).isEqualTo(task1)
            assertThat(this.currentEntity).isNull()
        }
    }

    @Test
    fun `createTaskLog should throw exception when logRepository returns failure`() {
        // Given
        val previousTask = null
        val currentTask = null
        every { logRepository.saveTaskLog(any()) } throws Exception("Error saving log")

        // When & Then
        assertThrows<Exception> {
            createTaskLogUseCase.createTaskLog(userId, previousTask, currentTask)
        }
    }
}