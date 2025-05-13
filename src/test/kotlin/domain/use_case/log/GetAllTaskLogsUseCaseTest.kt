package domain.use_case.log

import com.google.common.truth.Truth.assertThat
import creator_helper.taskLogsForAllUsers
import domain.repository.LogRepository
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test


class GetAllTaskLogsUseCaseTest {
    private lateinit var logRepository: LogRepository
    private lateinit var getAllTaskLogsUseCase: GetAllTaskLogsUseCase

    @BeforeEach
    fun setup() {
        logRepository = mockk()
        getAllTaskLogsUseCase = GetAllTaskLogsUseCase(logRepository)
    }

    @Test
    fun `getAllTaskLogs returns task logs from repository`() = runTest {
        // Given
        coEvery { logRepository.getAllTaskLogs() } returns taskLogsForAllUsers

        // When
        val result = getAllTaskLogsUseCase.getAllTaskLogs()

        // Then
        assertThat(result).isEqualTo(taskLogsForAllUsers)
        coVerify(exactly = 1) { logRepository.getAllTaskLogs() }
    }

    @Test
    fun `getAllTaskLogs returns empty list when repository returns empty`() = runTest {
        // Given
        coEvery { logRepository.getAllTaskLogs() } returns emptyList()

        // When
        val result = getAllTaskLogsUseCase.getAllTaskLogs()

        // Then
        assertThat(result).isEmpty()
        coVerify(exactly = 1) { logRepository.getAllTaskLogs() }
    }

    @Test
    fun `getAllTaskLogs throw exception from repository`() = runTest {
        // Given
        coEvery { logRepository.getAllTaskLogs() } throws RuntimeException("Database error")

        // When & Then
        assertThrows<RuntimeException> {
            getAllTaskLogsUseCase.getAllTaskLogs()
        }
        coVerify(exactly = 1) { logRepository.getAllTaskLogs() }
    }
}