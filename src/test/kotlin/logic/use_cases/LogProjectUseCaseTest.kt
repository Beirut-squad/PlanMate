package logic.use_cases

import com.google.common.truth.Truth.assertThat
import creator_helper.createProjectHelper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.logic.repositories.log_repository.LogRepository
import org.example.logic.use_cases.log.LogProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class LogProjectUseCaseTest {
    private val logRepository: LogRepository = mockk(relaxed = true)
    private lateinit var logProjectUseCase: LogProjectUseCase

    @BeforeEach
    fun setup() {
        logProjectUseCase = LogProjectUseCase(logRepository)
    }

    @Test
    fun `should return failure when previous and new project name are null`() {
        val userId = UUID.randomUUID()

        val result = logProjectUseCase.createProjectLog(userId, null, null)

        assert(result.isFailure)
    }

    @Test
    fun `should create project log and return success when both previous and new project name are not null`() {
        val userId = UUID.randomUUID()
        val previousProject = createProjectHelper(name = "Previous Project")
        val currentProject = createProjectHelper(name = "Current Project")
        every { logRepository.saveProjectLog(any()) } returns Result.success(Unit)

        val result = logProjectUseCase.createProjectLog(userId, previousProject, currentProject)

        assertThat(result.isSuccess)
        verify {
            logRepository.saveProjectLog(match { projectLog ->
                projectLog.userId == userId &&
                        projectLog.entityId == currentProject.id &&
                        projectLog.previousEntity == previousProject &&
                        projectLog.currentEntity == currentProject
            })
        }
    }

    @Test
    fun `should create project log and return success when previous project is null`() {
        val userId = UUID.randomUUID()
        val currentProject = createProjectHelper()
        every { logRepository.saveProjectLog(any()) } returns Result.success(Unit)

        val result = logProjectUseCase.createProjectLog(userId, null, currentProject)

        assert(result.isSuccess)
        verify {
            logRepository.saveProjectLog(match { projectLog ->
                projectLog.userId == userId &&
                        projectLog.previousEntity == null &&
                        projectLog.currentEntity == currentProject
            })
        }
    }

    @Test
    fun `should create project log and return success when current project is null`() {
        val userId = UUID.randomUUID()
        val previousProject = createProjectHelper()
        every { logRepository.saveProjectLog(any()) } returns Result.success(Unit)

        val result = logProjectUseCase.createProjectLog(userId, previousProject, null)

        assert(result.isSuccess)
        verify {
            logRepository.saveProjectLog(match { projectLog ->
                projectLog.userId == userId &&
                        projectLog.previousEntity == previousProject &&
                        projectLog.currentEntity == null
            })
        }
    }
}