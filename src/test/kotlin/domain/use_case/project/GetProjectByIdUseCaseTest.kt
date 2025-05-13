package domain.use_case.project

import creator_helper.createProjectHelper
import domain.repository.ProjectRepository
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.*
import java.util.UUID

class GetProjectByIdUseCaseTest {
    private val projectRepository: ProjectRepository = mockk(relaxed = true)
    private lateinit var getProjectByIdUseCase: GetProjectByIdUseCase

    @BeforeEach
    fun setUp() {
        clearAllMocks()
        getProjectByIdUseCase = GetProjectByIdUseCase(projectRepository)
    }

    @Test
    fun `getProjectById should call repository with correct ID`() = runTest {
        // Given
        val projectId = UUID.randomUUID()
        val expectedProject = createProjectHelper(id = projectId)
        coEvery { projectRepository.getProject(projectId) } returns expectedProject

        // When
        val result = getProjectByIdUseCase.getProjectById(projectId)

        // Then
        coVerify(exactly = 1) { projectRepository.getProject(projectId) }
        assertEquals(expectedProject, result)
    }

}