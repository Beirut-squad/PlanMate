package domain.use_case.project

import com.google.common.truth.Truth.assertThat
import creator_helper.createProjectHelper
import ui.common.exception.DuplicateDescriptionException
import ui.common.exception.EmptyProjectDescriptionException
import domain.model.Project
import domain.repository.ProjectRepository
import domain.use_case.log.CreateProjectLogUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertFailsWith

class EditProjectDescriptionUseCaseTest {

    private lateinit var projectRepository: ProjectRepository
    private lateinit var logUseCase: CreateProjectLogUseCase
    private lateinit var useCase: EditProjectDescriptionUseCase

    @BeforeEach
    fun setup() {
        projectRepository = mockk(relaxed = true)
        logUseCase = mockk(relaxed = true)
        useCase = EditProjectDescriptionUseCase(projectRepository, logUseCase)
    }

    @Test
    fun `given valid new description, when editProject called, then should update project and log`() = runTest {
        // Given
        val userId = UUID.randomUUID()
        val oldProject = createProjectHelper()
        val newDescription = "Updated description"

        // When
        useCase.editProject(oldProject, newDescription, userId)

        // Then
        coVerify(exactly = 1) {
            projectRepository.editProject(withArg {
                assertThat(it.description).isEqualTo(newDescription)
                assertThat(it.updatedAt).isNotEqualTo(oldProject.updatedAt)
            })
        }

        coVerify(exactly = 1) {
            logUseCase.createProjectLog(
                previousProject = oldProject,
                currentProject = any(),
                userId = userId
            )
        }
    }

    @Test
    fun `given blank description, when editProject called, then should throw EmptyProjectDescriptionException`() =
        runTest {
            val project = mockk<Project>()
            val userId = UUID.randomUUID()

            val exception = kotlin.runCatching {
                useCase.editProject(project, "", userId)
            }.exceptionOrNull()

            assertThat(exception).isInstanceOf(EmptyProjectDescriptionException::class.java)
            coVerify(exactly = 0) { projectRepository.editProject(any()) }
            coVerify(exactly = 0) { logUseCase.createProjectLog(any(), any(), any()) }
        }

    @Test
    fun `should throw EmptyProjectDescriptionException when description is null or blank`() = runTest {
        val userId = UUID.randomUUID()
        val project = createProjectHelper(description = "Original description")

        val useCase = EditProjectDescriptionUseCase(projectRepository, logUseCase)

        listOf(null, "", "   ").forEach { input ->
            assertFailsWith<EmptyProjectDescriptionException> {
                useCase.editProject(project, input, userId)
            }
        }

        coVerify(exactly = 0) { projectRepository.editProject(any()) }
        coVerify(exactly = 0) { logUseCase.createProjectLog(any(), any(), any()) }
    }

    @Test
    fun `should throw DuplicateDescriptionException if description is the same`() = runTest {
        val userId = UUID.randomUUID()
        val description = "Same description"
        val project = createProjectHelper(description = description)

        val useCase = EditProjectDescriptionUseCase(projectRepository, logUseCase)

        assertFailsWith<DuplicateDescriptionException> {
            useCase.editProject(project, description, userId)
        }

        coVerify(exactly = 0) { projectRepository.editProject(any()) }
        coVerify(exactly = 0) { logUseCase.createProjectLog(any(), any(), any()) }
    }
}
