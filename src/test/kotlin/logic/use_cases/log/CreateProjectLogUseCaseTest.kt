package logic.use_cases.log

import com.google.common.truth.Truth.assertThat
import creator_helper.createProjectHelper
import creator_helper.projectLog
import io.mockk.*
import org.example.logic.exceptions.log_exceptions.InvalidCreateProjectLogException
import org.example.logic.repositories.log_repository.LogRepository
import org.example.models.ProjectLog
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID

class CreateProjectLogUseCaseTest {
    private val logRepository: LogRepository = mockk(relaxed = true)
    private lateinit var createProjectLogUseCase: CreateProjectLogUseCase

    @BeforeEach
    fun setup() {
        createProjectLogUseCase = CreateProjectLogUseCase(logRepository)
    }

    @Test
    fun `createProjectLog should throw InvalidCreateProjectLogException when previous and current project are null`() {
        // Given
        val userId = UUID.randomUUID()
        val previousProject = null
        val currentProject = null

        // When & Then
        assertThrows<InvalidCreateProjectLogException> {
            createProjectLogUseCase.createProjectLog(userId, previousProject, currentProject)
        }
    }

    @Test
    fun `createProjectLog should create project log when both previous and current project are not null`() {
        // Given
        val userId = UUID.fromString("10000000-0000-0000-0000-000000000000")
        var capturedLog: ProjectLog? = null
        every { logRepository.saveProjectLog(any()) } answers {
            capturedLog = firstArg()
        }

        // When
        createProjectLogUseCase.createProjectLog(userId, projectLog.previousEntity, projectLog.currentEntity)

        // Then
        verify(exactly = 1) { logRepository.saveProjectLog(any()) }
        assertThat(capturedLog).isNotNull()
        with(capturedLog!!) {
            assertEquals(userId, this.userId)
            assertEquals(projectLog.entityId, this.entityId)
            assertEquals(projectLog.previousEntity, previousEntity)
            assertEquals(projectLog.currentEntity, currentEntity)
        }
    }

    @Test
    fun `createProjectLog should create project log and return success when previous project is null`() {
        // Given
        val userId = UUID.randomUUID()
        val currentProject = createProjectHelper()
        var capturedLog: ProjectLog? = null
        every { logRepository.saveProjectLog(any()) } answers {
            capturedLog = firstArg()
        }

        // When
        createProjectLogUseCase.createProjectLog(userId, null, currentProject)

        // Then
        verify(exactly = 1) { logRepository.saveProjectLog(any()) }
        assertThat(capturedLog).isNotNull()
        with(capturedLog!!) {
            assertEquals(userId, this.userId)
            assertEquals(currentProject.id, this.entityId)
            assertThat(previousEntity).isNull()
            assertEquals(currentProject, currentEntity)
        }
    }

    @Test
    fun `createProjectLog should create project log and return success when current project is null`() {
        // Given
        val userId = UUID.randomUUID()
        val previousProject = createProjectHelper()
        var capturedLog: ProjectLog? = null
        every { logRepository.saveProjectLog(any()) } answers {
            capturedLog = firstArg()
        }

        // When
        val result = createProjectLogUseCase.createProjectLog(userId, previousProject, null)

        // Then
        verify(exactly = 1) { logRepository.saveProjectLog(any()) }
        assertThat(capturedLog).isNotNull()
        with(capturedLog!!) {
            assertEquals(userId, this.userId)
            assertEquals(previousProject.id, this.entityId)
            assertThat(currentEntity).isNull()
            assertEquals(previousProject, previousEntity)
        }
    }
}
