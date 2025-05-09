package domain.use_case.project

import data.csv.model.Project
import org.example.domain.repository.ProjectRepository
import java.util.*

class GetUserProjectsByIdUseCase(private val repository: ProjectRepository) {
    suspend fun getProjectForUserById(userID: UUID): List<Project> {
        return repository.getProjectsForUserById(userID)
    }
}