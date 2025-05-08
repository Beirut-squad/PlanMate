package logic.use_case.project_manegement

import creator_helper.createProjectHelper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.logic.use_cases.project_manegment.GetAllProjectsUseCases
import org.example.models.Project
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.Exception

class GetAllProjectsUseCaseTest {
    private lateinit var projectRepository: ProjectRepository
    private lateinit var getAllProjectsUseCase: GetAllProjectsUseCases

    @BeforeEach
    fun setup() {
        projectRepository = mockk()
        getAllProjectsUseCase = GetAllProjectsUseCases(projectRepository)
    }

    @Test
    fun `should return all projects when call getAllProjects`() {
        // Given
        val projects = listOf(createProjectHelper(), createProjectHelper())
        every { projectRepository.getAllProjects() } returns Result.success(projects)

        // When
        val result = getAllProjectsUseCase.getAllProjects()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
        verify(exactly = 1) { projectRepository.getAllProjects() }
    }

    @Test
    fun `should return empty list when no projects exist`() {
        // Given
        every { projectRepository.getAllProjects() } returns Result.success(emptyList())

        // When
        val result = getAllProjectsUseCase.getAllProjects()

        // Then
        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()?.isEmpty() ?: false)
    }

    @Test
    fun `should propagate repository failure`() {
        // Given
        val expectedException = Exception("Database error")
        every { projectRepository.getAllProjects() } returns Result.failure(expectedException)

        // When
        val result = getAllProjectsUseCase.getAllProjects()

        // Then
        assertTrue(result.isFailure)
        assertEquals(expectedException, result.exceptionOrNull())
    }
}