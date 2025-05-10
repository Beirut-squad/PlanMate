package domain.use_case.state

import domain.exception.EmptyStateNameException
import domain.model.Project
import domain.model.State
import domain.use_case.authentication.GetCurrentUserUseCase
import domain.use_case.project.AddProjectStateUseCase
import java.util.*

class CreateStateUseCase(
    private val addProjectStateUseCase: AddProjectStateUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) {
    suspend fun createState(name: String, project: Project): State {
            if (name.isBlank()) throw EmptyStateNameException()
            val newState = State(id = UUID.randomUUID(), name = name)
            val currentUserId = getCurrentUserUseCase.getCurrentUser().id
            addProjectStateUseCase.addStateToProject(currentUserID = currentUserId, project = project, state = newState)
            return newState
    }
}