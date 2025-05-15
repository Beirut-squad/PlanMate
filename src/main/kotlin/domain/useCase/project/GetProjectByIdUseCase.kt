package domain.useCase.project

import domain.model.Project
import domain.repository.ProjectRepository
import java.util.*

class GetProjectByIdUseCase(
    private val repository: ProjectRepository,
) {
    suspend fun getProjectById(id: UUID): Project {
        return repository.getProject(id)
    }
}