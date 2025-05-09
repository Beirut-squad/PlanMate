package domain.use_case.project

import org.example.data.model.Project
import org.example.domain.repository.ProjectRepository
import java.util.*

class GetProjectByIdUseCase(
    private val repository: ProjectRepository,
) {
    suspend fun getProjectById(id: UUID): Project {
        return repository.getProject(id)
    }
}