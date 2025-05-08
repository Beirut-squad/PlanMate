package org.example.logic.use_cases.state_usecase

import org.example.logic.use_cases.authentication.GetCurrentLoggedInUserUseCase
import org.example.logic.use_cases.project_manegment.RemoveStateFromProjectUseCase
import org.example.models.Project
import org.example.models.State

class DeleteStateUseCase(
    private val removeStateFromProjectUseCase: RemoveStateFromProjectUseCase,
    private val getCurrentLoggedInUserUseCase: GetCurrentLoggedInUserUseCase
) {
    suspend fun deleteState(project: Project, state: State): Project {
        val currentUserId = getCurrentLoggedInUserUseCase
                    .getCurrentUser()?.id ?: throw IllegalArgumentException()

        return removeStateFromProjectUseCase
                    .removeStateFromProject(currentUserId, project, state)
    }
}