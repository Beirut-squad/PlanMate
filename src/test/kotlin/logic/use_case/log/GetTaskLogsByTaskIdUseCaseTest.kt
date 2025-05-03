package logic.use_case.log

import com.google.common.truth.Truth.assertThat
import creator_helper.taskLogsByTaskId
import creator_helper.testTaskId
import io.mockk.every
import io.mockk.mockk
import org.example.logic.repositories.log_repository.LogRepository
import org.example.logic.use_cases.log.GetTaskLogsByTaskIdUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetTaskLogsByTaskIdUseCaseTest {
    private val logRepository : LogRepository = mockk(relaxed = true)
    private lateinit var getTaskLogsByTaskIdUseCase : GetTaskLogsByTaskIdUseCase

    @BeforeEach
    fun setUp() {
        getTaskLogsByTaskIdUseCase = GetTaskLogsByTaskIdUseCase(logRepository)
    }

    @Test
    fun `should return task logs for a given task id when repository succeeds`() {
        every { logRepository.getTaskLogs(testTaskId) } returns Result.success(taskLogsByTaskId)

        val result = getTaskLogsByTaskIdUseCase.getTaskLogsByTaskId(testTaskId)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(taskLogsByTaskId)
    }

    @Test
    fun `should return failure when repository fails to get task logs`() {
        val exception = Exception("Error getting logs")
        every { logRepository.getTaskLogs(testTaskId) } returns Result.failure(exception)
        val result = getTaskLogsByTaskIdUseCase.getTaskLogsByTaskId(testTaskId)

        assertThat(result.isFailure)
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
    }
}