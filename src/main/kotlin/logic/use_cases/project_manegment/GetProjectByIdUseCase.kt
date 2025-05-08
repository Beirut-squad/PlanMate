package org.example.logic.use_cases.project_manegment

import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.models.Project
import java.util.UUID

class GetProjectByIdUseCase(
    private val repository: ProjectRepository,
) {
    fun getProjectById(id: UUID): Project {
        return repository.getProject(id)
    }
}