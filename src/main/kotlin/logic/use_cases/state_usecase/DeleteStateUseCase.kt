package org.example.logic.use_cases.state_usecase

import org.example.logic.use_cases.authentication.GetCurrentLoggedInUserUseCase
import org.example.logic.use_cases.project_manegment.RemoveStateFromProjectUseCase
import org.example.models.Project
import org.example.models.State
import java.util.*

class DeleteStateUseCase(
    private val removeStateFromProjectUseCase: RemoveStateFromProjectUseCase,
    private val getCurrentLoggedInUserUseCase: GetCurrentLoggedInUserUseCase
) {
    fun deleteState(project: Project, state: State): Result<Project> {
        val currentUserId = getCurrentLoggedInUserUseCase.getCurrentUser().getOrThrow()?.id ?: throw IllegalArgumentException()

        return removeStateFromProjectUseCase.removeStateFromProject(currentUserId, project, state)
    }
}