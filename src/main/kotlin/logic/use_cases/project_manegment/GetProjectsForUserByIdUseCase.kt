package org.example.logic.use_cases.project_manegment

import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.models.Project
import java.util.*

class GetProjectsForUserByIdUseCase(private val repository: ProjectRepository) {
    suspend fun getProjectForUserById(userID: UUID): List<Project> {
        return repository.getProjectsForUserById(userID)
    }
}