package domain.useCase.log

import com.google.common.truth.Truth.assertThat
import creator_helper.projectLogsForAllUsers
import domain.repository.LogRepository
import io.mockk.*
import kotlinx.coroutines.test.runTest
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
    fun `getAllProjectLogs returns project logs from repository`() = runTest {
        // Given
        coEvery { logRepository.getAllProjectLogs() } returns projectLogsForAllUsers

        // When
        val result = getAllProjectLogsUseCase.getAllProjectLogs()

        // Then
        assertThat(result).isEqualTo(projectLogsForAllUsers)
        coVerify(exactly = 1) { logRepository.getAllProjectLogs() }
    }

    @Test
    fun `getAllProjectLogs returns empty list when repository returns empty`() = runTest {
        // Given
        coEvery { logRepository.getAllProjectLogs() } returns emptyList()

        // When
        val result = getAllProjectLogsUseCase.getAllProjectLogs()

        // Then
        assertThat(result).isEmpty()
        coVerify(exactly = 1) { logRepository.getAllProjectLogs() }
    }

    @Test
    fun `getAllProjectLogs propagates exceptions from repository`() = runTest {
        // Given
        coEvery { logRepository.getAllProjectLogs() } throws RuntimeException("Database error")

        // When & Then
        assertThrows<RuntimeException> {
            getAllProjectLogsUseCase.getAllProjectLogs()
        }
        coVerify(exactly = 1) { logRepository.getAllProjectLogs() }
    }
}