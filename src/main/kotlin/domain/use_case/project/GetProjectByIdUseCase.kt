package domain.use_case.project

import domain.model.Project
import org.example.domain.repository.ProjectRepository
import java.util.*

class GetProjectByIdUseCase(
    private val repository: ProjectRepository,
) {
    suspend fun getProjectById(id: UUID): Project {
        return repository.getProject(id)
    }
}