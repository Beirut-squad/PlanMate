package logic.use_case.project_manegement

import creator_helper.createProjectHelper
import io.mockk.mockk
import io.mockk.verify
import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.logic.use_case.project_manegement.CreateProjectUseCase
import org.example.models.Project
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CreateProjectUseCaseTest {
    private val projectRepository: ProjectRepository = mockk(relaxed = true)
    private lateinit var createProjectUseCase: CreateProjectUseCase
    private val project: Project = mockk()

    @BeforeEach
    fun setup() {
        createProjectUseCase = CreateProjectUseCase(projectRepository)
    }

    @Test
    fun `should create project successfully when call createProject function`() {
        // Given
        val project = createProjectHelper()

        // When
        createProjectUseCase.createProject()

        // Then
        verify(exactly = 1) { projectRepository.createProject(project) }

    }


}