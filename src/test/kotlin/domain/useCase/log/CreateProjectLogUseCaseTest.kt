package domain.useCase.log

import com.google.common.truth.Truth.assertThat
import creator_helper.projectLog
import creator_helper.testUserId
import domain.model.ProjectLog
import domain.repository.LogRepository
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ui.common.exception.NullProjectsComparisonException

class CreateProjectLogUseCaseTest {

    private val logRepository: LogRepository = mockk(relaxed = true)
    private lateinit var createProjectLogUseCase: CreateProjectLogUseCase

    @BeforeEach
    fun setup() {
        createProjectLogUseCase = CreateProjectLogUseCase(logRepository)
    }

    @Test
    fun `createProjectLog should throw NullProjectsComparisonException when previous and current project are null`() =
        runTest {
            // Given
            val previousProject = null
            val currentProject = null

            // When & Then
            assertThrows<NullProjectsComparisonException> {
                createProjectLogUseCase.createProjectLog(testUserId, previousProject, currentProject)
            }
        }

    @Test
    fun `createProjectLog should create project log when both previous and current project are not null`() = runTest {
        // Given
        var capturedLog: ProjectLog? = null
        coEvery { logRepository.saveProjectLog(any()) } answers {
            capturedLog = firstArg()
        }

        // When
        createProjectLogUseCase.createProjectLog(testUserId, projectLog.previousEntity, projectLog.currentEntity)

        // Then
        coVerify(exactly = 1) { logRepository.saveProjectLog(any()) }
        assertThat(capturedLog).isNotNull()
        with(capturedLog!!) {
            assertEquals(userId, this.userId)
            assertEquals(projectLog.entityId, this.entityId)
            assertEquals(projectLog.previousEntity, previousEntity)
            assertEquals(projectLog.currentEntity, currentEntity)
        }
    }

    @Test
    fun `createProjectLog should create project log when previous project is null`() = runTest {
        // Given
        val currentProject = projectLog.currentEntity
        var capturedLog: ProjectLog? = null
        coEvery { logRepository.saveProjectLog(any()) } answers {
            capturedLog = firstArg()
        }

        // When
        createProjectLogUseCase.createProjectLog(testUserId, null, currentProject)

        // Then
        coVerify(exactly = 1) { logRepository.saveProjectLog(any()) }
        assertThat(capturedLog).isNotNull()
        with(capturedLog!!) {
            assertEquals(userId, this.userId)
            assertEquals(currentProject?.id, this.entityId)
            assertThat(previousEntity).isNull()
            assertEquals(currentProject, currentEntity)
        }
    }

    @Test
    fun `createProjectLog should create project log when current project is null`() = runTest {
        // Given
        val previousProject = projectLog.previousEntity
        var capturedLog: ProjectLog? = null
        coEvery { logRepository.saveProjectLog(any()) } answers {
            capturedLog = firstArg()
        }

        // When
        createProjectLogUseCase.createProjectLog(testUserId, previousProject, null)

        // Then
        coVerify(exactly = 1) { logRepository.saveProjectLog(any()) }
        assertThat(capturedLog).isNotNull()
        with(capturedLog!!) {
            assertEquals(userId, this.userId)
            assertEquals(previousProject?.id, this.entityId)
            assertThat(currentEntity).isNull()
            assertEquals(previousProject, previousEntity)
        }
    }

    @Test
    fun `createProjectLog should throw exception when saveProjectLog returns failure`() = runTest {
        // Given
        val previousProject = null
        val currentProject = null
        coEvery { logRepository.saveProjectLog(any()) } throws Exception("Error saving log")

        // When & Then
        assertThrows<Exception> {
            createProjectLogUseCase.createProjectLog(testUserId, previousProject, currentProject)
        }
    }
}
