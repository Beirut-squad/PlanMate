package logic.use_case.project_manegement

import creator_helper.createProjectHelper
import io.mockk.mockk
import io.mockk.verify
import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.logic.use_case.project_manegement.GetAllProjectsUseCase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class GetAllProjectsUseCaseTest{
  private val projectRepository: ProjectRepository = mockk()
  private lateinit var getAllProjectsUseCase :GetAllProjectsUseCase

  @BeforeEach
  fun setup(){
   getAllProjectsUseCase = GetAllProjectsUseCase(projectRepository)
  }
    @Test
    fun `should return all projects when call getAllProjects fun`(){
        //Given
        getAllProjectsUseCase.getAllProjects()

        //When
        projectRepository.getAllProjects()

        //Then
        verify(exactly = 1) {  projectRepository.getAllProjects() }
    }

 }