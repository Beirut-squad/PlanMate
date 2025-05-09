package logic.use_cases.log

import com.google.common.truth.Truth.assertThat
import creator_helper.taskLogsForAllUsers
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.logic.repositories.log_repository.LogRepository
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
    fun `getAllTaskLogs returns logs from repository`() {
        // Given
        every { logRepository.getAllTaskLogs() } returns taskLogsForAllUsers

        // When
        val result = getAllTaskLogsUseCase.getAllTaskLogs()

        // Then
        assertThat(result).isEqualTo(taskLogsForAllUsers)
        verify(exactly = 1) { logRepository.getAllTaskLogs() }
    }

    @Test
    fun `getAllTaskLogs returns empty list when repository returns empty`() {
        // Given
        every { logRepository.getAllTaskLogs() } returns emptyList()

        // When
        val result = getAllTaskLogsUseCase.getAllTaskLogs()

        // Then
        assertThat(result).isEmpty()
        verify(exactly = 1) { logRepository.getAllTaskLogs() }
    }

    @Test
    fun `getAllTaskLogs throw exception from repository`() {
        // Given
        every { logRepository.getAllTaskLogs() } throws RuntimeException("Database error")

        // When & Then
        assertThrows<RuntimeException> {
            getAllTaskLogsUseCase.getAllTaskLogs()
        }
        verify(exactly = 1) { logRepository.getAllTaskLogs() }
    }
}