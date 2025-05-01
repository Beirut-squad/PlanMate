package logic.use_cases.log

import com.google.common.truth.Truth.assertThat
import creator_helper.createTaskHelper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.logic.repositories.log_repository.LogRepository
import org.junit.jupiter.api.BeforeEach
import java.util.UUID
import kotlin.test.Test
import org.junit.jupiter.api.Assertions.assertTrue

class CreateTaskLogUseCaseTest {

    private val logRepository: LogRepository = mockk(relaxed = true)
    private lateinit var createTaskLogUseCase: CreateTaskLogUseCase

    @BeforeEach
    fun setUp() {
        createTaskLogUseCase = CreateTaskLogUseCase(logRepository)
    }

    @Test
    fun `should return failure when previous and current task are null`() {
        val userId = UUID.randomUUID()

        val result = createTaskLogUseCase.createTaskLog(userId, null, null)

        assertTrue(result.isFailure)
    }

    @Test
    fun `should create task log and return success when both previous and current task are not null`() {
        val userId = UUID.randomUUID()
        val projectId = UUID.randomUUID()
        val previousTask = createTaskHelper(title = "Previous Task", projectId = projectId)
        val currentTask = createTaskHelper(title = "Current Task", projectId = projectId)
        every { logRepository.saveTaskLog(any()) } returns Result.success(Unit)

        val result = createTaskLogUseCase.createTaskLog(userId, previousTask, currentTask)

        assertThat(result.isSuccess)
        verify {
            logRepository.saveTaskLog(match { taskLog ->
                taskLog.userId == userId &&
                        taskLog.entityId == currentTask.id &&
                        taskLog.previousEntity == previousTask &&
                        taskLog.currentEntity == currentTask
            })
        }
    }

    @Test
    fun `should create task log and return success when previous task is null`() {
        val userId = UUID.randomUUID()
        val projectId = UUID.randomUUID()
        val previousTask = createTaskHelper(title = "Previous Task", projectId = projectId)
        every { logRepository.saveTaskLog(any()) } returns Result.success(Unit)

        val result = createTaskLogUseCase.createTaskLog(userId, null, previousTask)

        assert(result.isSuccess)
        verify {
            logRepository.saveTaskLog(match { taskLog ->
                taskLog.userId == userId &&
                        taskLog.previousEntity == null &&
                        taskLog.currentEntity == previousTask
            })
        }
    }

    @Test
    fun `should create task log and return success when current task is null`() {
        val userId = UUID.randomUUID()
        val projectId = UUID.randomUUID()
        val currentTask = createTaskHelper(title = "Current Task", projectId = projectId)
        every { logRepository.saveTaskLog(any()) } returns Result.success(Unit)

        val result = createTaskLogUseCase.createTaskLog(userId, currentTask, null)

        assert(result.isSuccess)
        verify {
            logRepository.saveTaskLog(match { taskLog ->
                taskLog.userId == userId &&
                        taskLog.previousEntity == currentTask &&
                        taskLog.currentEntity == null
            })
        }
    }

    @Test
    fun `should return failure when logRepository returns failure`() {
        val userId = UUID.randomUUID()
        val projectId = UUID.randomUUID()
        val previousTask = createTaskHelper(title = "Previous Task", projectId = projectId)
        val currentTask = createTaskHelper(title = "Current Task", projectId = projectId)
        val exception = Exception("Error saving log")

        every { logRepository.saveTaskLog(any()) } returns Result.failure(exception)

        val result = createTaskLogUseCase.createTaskLog(userId, previousTask, currentTask)

        assert(result.isFailure)
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
    }
}