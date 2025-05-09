package domain.use_case.state

import org.example.data.model.Project
import org.example.data.model.State
import domain.use_case.authentication.GetCurrentUserUseCase
import domain.use_case.project.DeleteProjectStateUseCase

class DeleteStateUseCase(
    private val deleteProjectStateUseCase: DeleteProjectStateUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) {
    suspend fun deleteState(project: Project, state: State): Project {
        val currentUserId = getCurrentUserUseCase
                    .getCurrentUser()?.id ?: throw IllegalArgumentException()

        return deleteProjectStateUseCase
                    .removeStateFromProject(currentUserId, project, state)
    }
}