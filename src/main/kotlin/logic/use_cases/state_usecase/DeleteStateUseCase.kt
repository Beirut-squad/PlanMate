package org.example.logic.use_cases.state_usecase

import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.logic.repositories.state_repository.StateRepository
import org.example.logic.use_cases.project_manegment.RemoveStateFromProjectUseCase
import org.example.models.State
import java.util.UUID

class DeleteStateUseCase(
    private val removeStateFromProjectUseCase: RemoveStateFromProjectUseCase
) {
    fun deleteState(projectId: UUID, state: State): Result<Unit> {
        return removeStateFromProjectUseCase.removeStateFromProject(projectId, state)
    }
}