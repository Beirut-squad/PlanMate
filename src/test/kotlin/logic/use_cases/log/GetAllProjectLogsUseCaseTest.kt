package logic.use_cases.log

import com.google.common.truth.Truth.assertThat
import creator_helper.projectLogsForAllUsers
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.logic.repositories.log_repository.LogRepository
import org.example.logic.use_cases.log.GetAllProjectLogsUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class GetAllProjectLogsUseCaseTest {

    private lateinit var logRepository: LogRepository
    private lateinit var getAllProjectLogsUseCase: GetAllProjectLogsUseCase

    @BeforeEach
    fun setup() {
        logRepository = mockk()
        getAllProjectLogsUseCase = GetAllProjectLogsUseCase(logRepository)
    }

    @Test
    fun `getAllProjectLogs returns logs from repository`() {
        // Given
        every { logRepository.getAllProjectLogs() } returns projectLogsForAllUsers

        // When
        val result = getAllProjectLogsUseCase.getAllProjectLogs()

        // Then
        assertThat(result).isEqualTo(projectLogsForAllUsers)
        verify(exactly = 1) { logRepository.getAllProjectLogs() }
    }

    @Test
    fun `getAllProjectLogs returns empty list when repository returns empty`() {
        // Given
        every { logRepository.getAllProjectLogs() } returns emptyList()

        // When
        val result = getAllProjectLogsUseCase.getAllProjectLogs()

        // Then
        assertThat(result).isEmpty()
        verify(exactly = 1) { logRepository.getAllProjectLogs() }
    }

    @Test
    fun `getAllProjectLogs propagates exceptions from repository`() {
        // Given
        every { logRepository.getAllProjectLogs() } throws RuntimeException("Database error")

        // When & Then
        assertThrows<RuntimeException> {
            getAllProjectLogsUseCase.getAllProjectLogs()
        }
        verify(exactly = 1) { logRepository.getAllProjectLogs() }
    }
}