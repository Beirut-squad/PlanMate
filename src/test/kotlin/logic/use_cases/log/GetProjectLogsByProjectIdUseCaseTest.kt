package logic.use_cases.log

import com.google.common.truth.Truth.assertThat
import creator_helper.projectLogsByProjectId
import creator_helper.testProjectId
import io.mockk.every
import io.mockk.mockk
import org.example.logic.repositories.log_repository.LogRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetProjectLogsByProjectIdUseCaseTest {
    private val logRepository : LogRepository = mockk(relaxed = true)
     private lateinit var getProjectLogsByProjectIdUseCase : GetProjectLogsByProjectIdUseCase

     @BeforeEach
     fun setUp() {
         getProjectLogsByProjectIdUseCase = GetProjectLogsByProjectIdUseCase(logRepository)
     }

    @Test
    fun `should return project logs for a given project id when repository succeeds`() {
        every { logRepository.getProjectLogs(testProjectId) } returns Result.success(projectLogsByProjectId)

        val result = getProjectLogsByProjectIdUseCase.getProjectLogsByProjectId(testProjectId)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(projectLogsByProjectId)
    }

    @Test
    fun `should return failure when repository fails to get project logs`() {
        val exception = Exception("Error getting logs")
        every { logRepository.getProjectLogs(testProjectId) } returns Result.failure(exception)
        val result = getProjectLogsByProjectIdUseCase.getProjectLogsByProjectId(testProjectId)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
    }

}