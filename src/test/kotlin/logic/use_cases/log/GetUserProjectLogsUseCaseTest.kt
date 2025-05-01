package logic.use_cases.log

import com.google.common.truth.Truth.assertThat
import creator_helper.projectLogsForAllUsers
import creator_helper.projectLogsForTestUser
import creator_helper.testUserId
import io.mockk.every
import io.mockk.mockk
import org.example.logic.repositories.log_repository.LogRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetUserProjectLogsUseCaseTest {

    private val logRepository: LogRepository = mockk(relaxed = true)
    private lateinit var getUserProjectLogsUseCase: GetUserProjectLogsUseCase

    @BeforeEach
    fun setUp() {
        getUserProjectLogsUseCase = GetUserProjectLogsUseCase(logRepository)
    }

    @Test
    fun `should return project logs for a given user when repository succeeds`() {
        every { logRepository.getAllProjectLogs() } returns Result.success(projectLogsForAllUsers)

        val result = getUserProjectLogsUseCase.getUserProjectLogs(testUserId)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).hasSize(projectLogsForTestUser.size)
    }

    @Test
    fun `should return failure when repository fails to get project logs`() {
        val exception = Exception("Error getting logs")
        every { logRepository.getAllProjectLogs() } returns Result.failure(exception)

        val result = getUserProjectLogsUseCase.getUserProjectLogs(testUserId)

        assert(result.isFailure)
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
    }
}