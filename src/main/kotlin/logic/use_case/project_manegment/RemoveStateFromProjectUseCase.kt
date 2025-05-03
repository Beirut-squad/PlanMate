package org.example.logic.use_case.project_manegment

import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.models.State
import java.util.*

class RemoveStateFromProjectUseCase(
    private val repository: ProjectRepository,
) {
    fun removeStateFromProject(projectId: UUID, state: State): Result<Unit> {
        return repository.removeStateFromProject(projectId, state)
    }
}