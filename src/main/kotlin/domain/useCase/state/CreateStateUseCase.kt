package domain.useCase.state

import ui.common.exception.EmptyStateNameException
import domain.model.Project
import domain.model.TaskState
import domain.useCase.authentication.GetCurrentUserUseCase
import domain.useCase.project.AddProjectStateUseCase
import java.util.*

class CreateStateUseCase(
    private val addProjectStateUseCase: AddProjectStateUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) {
    suspend fun createState(name: String, project: Project): TaskState {
            if (name.isBlank()) throw EmptyStateNameException()
            val newTaskState = TaskState(id = UUID.randomUUID(), name = name)
            val currentUserId = getCurrentUserUseCase.getCurrentUser().id
            addProjectStateUseCase.addStateToProject(currentUserID = currentUserId, project = project, taskState = newTaskState)
            return newTaskState
    }
}