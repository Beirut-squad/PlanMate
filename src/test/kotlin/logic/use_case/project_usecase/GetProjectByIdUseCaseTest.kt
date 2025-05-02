package logic.use_case.project_usecase

import com.google.common.truth.Truth.assertThat
import creator_helper.createProjectHelper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.constants.StringConstants
import org.example.logic.exceptions.NoProjectFoundException
import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.logic.use_case.project_usecase.GetProjectByIdUseCase
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class GetProjectByIdUseCaseTest {

    private lateinit var repository: ProjectRepository
    private lateinit var useCase: GetProjectByIdUseCase

    @BeforeEach
    fun setUp() {
        repository = mockk(relaxed = true)
        useCase = GetProjectByIdUseCase(repository)
    }

    //region Test cases for getProject()
    @Test
    fun `getProjectById should call getProject from ProjectRepository exactly once`() {
        // Given
        val project = createProjectHelper()
        every { repository.getProject(project.id) } returns Result.success(project)

        // When
        val result = useCase.getProjectById(project.id)

        // Then
        verify(exactly = 1) {
            repository.getProject(project.id)
        }
    }

    @Test
    fun `getProjectById should return success with project when project exists`() {
        // Given
        val project = createProjectHelper()
        every { repository.getProject(project.id) } returns Result.success(project)

        // When
        val result = useCase.getProjectById(project.id)

        // Then
        assertTrue(result.isSuccess)
        val expectedProject = result.getOrNull()
        assertNotNull(expectedProject)
        assertThat(expectedProject?.id).isEqualTo(project.id)
    }

    @Test
    fun `getProjectById should return failure with NoProjectFoundException when project not found`() {
        // Given
        val project = createProjectHelper()
        every { repository.getProject(project.id) } returns Result.failure(NoProjectFoundException())

        // When
        val result = useCase.getProjectById(project.id)

        // Then
        kotlin.test.assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        kotlin.test.assertTrue(exception is NoProjectFoundException)
        kotlin.test.assertEquals(StringConstants.Project.NO_PROJECT_FOUND, exception.message)
    }
    //endregion

}