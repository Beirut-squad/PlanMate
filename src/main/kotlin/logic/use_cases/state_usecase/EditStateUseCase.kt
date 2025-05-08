package logic.use_cases.state_usecase

import org.example.logic.use_cases.authentication.GetCurrentLoggedInUserUseCase
import org.example.logic.use_cases.project_manegment.EditStateToProjectUseCase
import org.example.models.Project
import org.example.models.State

class EditStateUseCase(
    private val editStateToProjectUseCase: EditStateToProjectUseCase,
    private val getCurrentLoggedInUserUseCase: GetCurrentLoggedInUserUseCase
) {
    suspend fun editState(stateToEdit: State, newName: String, project: Project): Project{
        if (newName.isEmpty()) throw IllegalArgumentException("Edit failed: name cannot be blank!")

        val currentUserId = getCurrentLoggedInUserUseCase.getCurrentUser()?.id
            ?: throw Exception("User not logged in")

        val updatedState = stateToEdit.copy(name = newName)

        val updatedStates = project.state.map { state ->
            if (state.id == stateToEdit.id) updatedState else state
        }

        editStateToProjectUseCase.editStateToProject(currentUserId, project, updatedState)
        
        return project.copy(state = updatedStates)
    }
}