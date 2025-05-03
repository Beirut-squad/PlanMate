package org.example.logic.use_cases.project_manegment

import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.models.State
import java.util.*

class AddStateToProjectUseCase(
    private val repository: ProjectRepository
) {
    fun addStateToProject(projectId: UUID, state: State): Result<Unit> {
        return repository.addStateToProject(projectId, state)
    }
}
