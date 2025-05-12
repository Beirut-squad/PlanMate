package domain.use_case.project

import domain.model.Project
import domain.repository.ProjectRepository
import java.util.*

class GetUserProjectsByIdUseCase(private val repository: ProjectRepository) {
    suspend fun getProjectForUserById(userID: UUID): List<Project> {
        return repository.getProjectsForUserById(userID)
    }
}