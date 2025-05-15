package domain.useCase.state

import domain.model.Project
import domain.model.TaskState
import domain.useCase.authentication.GetCurrentUserUseCase
import domain.useCase.project.DeleteProjectStateUseCase

class DeleteStateUseCase(
    private val deleteProjectStateUseCase: DeleteProjectStateUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) {
    suspend fun deleteState(project: Project, taskState: TaskState): Project {
        val currentUserId = getCurrentUserUseCase.getCurrentUser().id

        return deleteProjectStateUseCase
                    .removeStateFromProject(currentUserId, project, taskState)
    }
}