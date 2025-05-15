package domain.useCase.state

import ui.common.exception.EmptyStateNameException
import domain.model.Project
import domain.model.TaskState
import domain.useCase.authentication.GetCurrentUserUseCase
import domain.useCase.project.EditProjectStateUseCase

class EditStateUseCase(
    private val editProjectStateUseCase: EditProjectStateUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) {
    suspend fun editState(taskStateToEdit: TaskState, newName: String, project: Project): Project {
        if (newName.isEmpty()) throw EmptyStateNameException()

        val currentUserId = getCurrentUserUseCase.getCurrentUser().id

        val updatedState = taskStateToEdit.copy(name = newName)

        val updatedStates = project.taskStates.map { state ->
            if (state.id == taskStateToEdit.id) updatedState else state
        }

        editProjectStateUseCase.editStateToProject(currentUserId, project, updatedState)
        
        return project.copy(taskStates = updatedStates)
    }
}