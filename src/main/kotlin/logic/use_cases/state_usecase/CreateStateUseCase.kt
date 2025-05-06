package org.example.logic.use_cases.state_usecase

import org.example.logic.use_cases.authentication.GetCurrentLoggedInUserUseCase
import org.example.logic.use_cases.project_manegment.AddStateToProjectUseCase
import org.example.models.Project
import org.example.models.State
import java.util.*

class CreateStateUseCase(
    private val addStateToProjectUseCase: AddStateToProjectUseCase,
    private val getCurrentLoggedInUserUseCase: GetCurrentLoggedInUserUseCase
) {
    fun createState(name: String, project: Project): Result<State> {
        return runCatching {
            if (name.isBlank()) throw (IllegalArgumentException("Create failed : name is Blank !!"))
            val newState = State(id = UUID.randomUUID(), name = name)
            val currentUserId = getCurrentLoggedInUserUseCase.getCurrentUser().getOrThrow()?.id ?: throw Exception()
            addStateToProjectUseCase.addStateToProject(currentUserID = currentUserId, project = project, state = newState)
            newState
        }
    }
}