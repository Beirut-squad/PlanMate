package domain.use_case.state

import data.csv.model.Project
import data.csv.model.State
import domain.use_case.authentication.GetCurrentUserUseCase
import domain.use_case.project.EditProjectStateUseCase

class EditStateUseCase(
    private val editProjectStateUseCase: EditProjectStateUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) {
    suspend fun editState(stateToEdit: State, newName: String, project: Project): Project {
        if (newName.isEmpty()) throw IllegalArgumentException("Edit failed: name cannot be blank!")

        val currentUserId = getCurrentUserUseCase.getCurrentUser()?.id
            ?: throw Exception("User not logged in")

        val updatedState = stateToEdit.copy(name = newName)

        val updatedStates = project.state.map { state ->
            if (state.id == stateToEdit.id) updatedState else state
        }

        editProjectStateUseCase.editStateToProject(currentUserId, project, updatedState)
        
        return project.copy(state = updatedStates)
    }
}