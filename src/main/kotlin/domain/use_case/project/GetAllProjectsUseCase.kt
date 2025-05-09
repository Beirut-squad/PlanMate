package domain.use_case.project

import org.example.data.model.Project
import org.example.domain.repository.ProjectRepository

class GetAllProjectsUseCase(
    private val projectRepository: ProjectRepository
) {
    suspend fun getAllProjects(): List<Project> {
        return projectRepository.getAllProjects()
    }
}