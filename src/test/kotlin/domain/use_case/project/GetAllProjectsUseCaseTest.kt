package domain.use_case.project

import creator_helper.createProjectHelper
import domain.repository.ProjectRepository
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.*

class GetAllProjectsUseCaseTest {

    private var projectRepository: ProjectRepository = mockk(relaxed = true)
    private lateinit var getAllProjectsUseCase: GetAllProjectsUseCase

    @BeforeEach
    fun setUp() {
        clearAllMocks()

        getAllProjectsUseCase = GetAllProjectsUseCase(projectRepository)
    }

    @Test
    fun `getAllProjects should call repository once`() = runTest {
        // Given
        val projectList = listOf(createProjectHelper(), createProjectHelper())

        coEvery { projectRepository.getAllProjects() } returns projectList

        // When
        val result = getAllProjectsUseCase.getAllProjects()

        // Then
        coVerify(exactly = 1) { projectRepository.getAllProjects() }
        assertEquals(2, result.size)
    }
}