package org.example.logic.use_case.project_usecase

import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.models.Project
import java.util.UUID

class GetProjectByIdUseCase(
    private val repository: ProjectRepository,
) {
    fun getProjectById(id: UUID): Result<Project> {
        return repository.getProject(id)
    }
}