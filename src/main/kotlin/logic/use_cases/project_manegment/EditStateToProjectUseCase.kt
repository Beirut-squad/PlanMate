package org.example.logic.use_cases.project_manegment

import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.models.State
import java.util.*

class EditStateToProjectUseCase(
    private val repository: ProjectRepository
) {
    fun editStateToProject(projectId: UUID, state: State): Result<Unit> {
        return repository.editStateToProject(projectId, state)
    }
}