package logic.use_case.project_manegement

import io.mockk.mockk
import io.mockk.verify
import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.logic.use_case.project_manegement.CreateProjectUseCase
import org.example.models.Project
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class CreateProjectUseCaseTest {
    private val projectRepository: ProjectRepository = mockk(relaxed = true)
    private lateinit var createProjectUseCase: CreateProjectUseCase
    @BeforeEach
    fun setup() {
        createProjectUseCase = CreateProjectUseCase(projectRepository)
    }
    @Test
    fun `should create project successfully when call createProject function`() {
        // Given
        val userId = UUID.randomUUID()
        val name = "Test Project"
        val description = "Test Description"
        val stateNames = listOf("Todo", "In Progress", "Done")

        // When
        createProjectUseCase.createProject(
            userId = userId,
            name = name,
            description = description,
            stateNames = stateNames
        )

        // Then
        verify(exactly = 1) {
            projectRepository.createProject(match { project ->
                project.name == name &&
                        project.description == description &&
                        project.creatorUserID == userId &&
                        project.state.size == stateNames.size &&
                        project.state.map { it.name } == stateNames
            })
        }
    }
}

