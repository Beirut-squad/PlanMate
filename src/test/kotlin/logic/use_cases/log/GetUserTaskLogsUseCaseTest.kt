package logic.use_cases.log

import com.google.common.truth.Truth.assertThat
import creator_helper.taskLogsForAllUsers
import creator_helper.taskLogsForTestUser
import creator_helper.testUserId
import io.mockk.every
import io.mockk.mockk
import org.example.logic.repositories.log_repository.LogRepository
import org.example.logic.use_cases.log.GetUserTaskLogsUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetUserTaskLogsUseCaseTest {
    private val logRepository: LogRepository = mockk(relaxed = true)
    private lateinit var getUserTaskLogsUseCase: GetUserTaskLogsUseCase

    @BeforeEach
    fun setUp() {
        getUserTaskLogsUseCase = GetUserTaskLogsUseCase(logRepository)
    }

    @Test
    fun `should return task logs for a given user when repository succeeds`() {
        every { logRepository.getAllTaskLogs() } returns Result.success(taskLogsForAllUsers)

        val result = getUserTaskLogsUseCase.getUserTaskLogs(testUserId)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).hasSize(taskLogsForTestUser.size)
    }

    @Test
    fun `should return failure when repository fails to get task logs`() {
        val exception = Exception("Error getting logs")
        every { logRepository.getAllTaskLogs() } returns Result.failure(exception)

        val result = getUserTaskLogsUseCase.getUserTaskLogs(testUserId)

        assert(result.isFailure)
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
    }
}