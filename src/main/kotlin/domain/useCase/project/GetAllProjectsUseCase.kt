package domain.useCase.project

import domain.model.Project
import domain.repository.ProjectRepository

class GetAllProjectsUseCase(
    private val projectRepository: ProjectRepository
) {
    suspend fun getAllProjects(): List<Project> {
        return projectRepository.getAllProjects()
    }
}