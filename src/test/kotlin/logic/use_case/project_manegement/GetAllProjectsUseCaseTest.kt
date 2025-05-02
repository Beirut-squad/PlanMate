package logic.use_case.project_manegement

import creator_helper.createProjectHelper
import io.mockk.mockk
import io.mockk.verify
import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.logic.use_case.project_manegment.GetAllProjectsUseCases
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetAllProjectsUseCaseTest {
    private val projectRepository: ProjectRepository = mockk(relaxed = true)
    //private val logUseCase: CreateProjectLogUseCase = mockk(relaxed = true)
    private var getAllProjectsUseCase: GetAllProjectsUseCases = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        getAllProjectsUseCase = GetAllProjectsUseCases(projectRepository)
    }

    @Test
    fun `should return all projects when call getAllProjects fun`() {
        //Given
        getAllProjectsUseCase.getAllProjects()

        //When
        projectRepository.getAllProjects()

        //Then
        verify { projectRepository.getAllProjects() }
    }

}