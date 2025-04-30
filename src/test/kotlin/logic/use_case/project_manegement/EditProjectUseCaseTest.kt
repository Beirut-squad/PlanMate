package logic.use_case.project_manegement

import creator_helper.createProjectHelper
import io.mockk.mockk
import io.mockk.verify
import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.logic.use_case.project_manegement.EditProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class EditProjectUseCaseTest {
    private val projectRepository: ProjectRepository = mockk(relaxed = true)
    private lateinit var editProjectUseCase: EditProjectUseCase

    @BeforeEach
    fun setup() {
        editProjectUseCase = EditProjectUseCase(projectRepository)
    }

    @Test
    fun `should edit project successfully when call edit Project Name function`() {
        //Given
        val project = createProjectHelper()
        val userId = UUID.randomUUID()
        editProjectUseCase.editProjectName(
            project = project,
            newName = "Bassant",
            userId = userId
        )
        //When
        projectRepository.editProject(project)
        //Then
        verify(exactly = 1) { projectRepository.editProject(project) }
    }

    @Test
    fun `should edit project successfully when call edit project description function`() {
        //Given
        val project = createProjectHelper()
        val userId = UUID.randomUUID()
        editProjectUseCase.editProjectDescription(
            project = project,
            newDescription = "Bassant",
            userId = userId
        )
        //When
        projectRepository.editProject(project)
        //Then
        verify(exactly = 1) { projectRepository.editProject(project) }
    }

    @Test
    fun `should edit project successfully when call edit project name and description function`() {
        //Given
        val project = createProjectHelper()
        val userId = UUID.randomUUID()
        editProjectUseCase.editProjectNameAndDescription(
            project = project,
            newName = "",
            newDescription = "Bassant",
            userId = userId
        )
        //When
        projectRepository.editProject(project)
        //Then
        verify(exactly = 1) { projectRepository.editProject(project) }
    }
}
