package domain.useCase.project

import creator_helper.createProjectHelper
import creator_helper.createUserHelper
import domain.repository.ProjectRepository
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.*

class GetUserProjectsByIdUseCaseTest {
    private val projectRepository: ProjectRepository = mockk(relaxed = true)
    private lateinit var getUserProjectsByIdUseCase: GetUserProjectsByIdUseCase

    @BeforeEach
    fun setUp() {
        clearAllMocks()
        getUserProjectsByIdUseCase = GetUserProjectsByIdUseCase(projectRepository)
    }

    @Test
    fun `getProjectForUserById should call repository with correct user ID`() = runTest {
        // Given
        val user = createUserHelper()
        val projectList = listOf(createProjectHelper(users = listOf(user)), createProjectHelper(users = listOf(user)))
        coEvery { projectRepository.getUserProjectsById(user.id) } returns projectList

        // When
        val result = getUserProjectsByIdUseCase.getProjectForUserById(user.id)

        // Then
        coVerify(exactly = 1) { projectRepository.getUserProjectsById(user.id) }
        assertEquals(projectList, result)
    }

}