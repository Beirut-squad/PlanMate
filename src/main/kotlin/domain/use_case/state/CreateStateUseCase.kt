package domain.use_case.state

import org.example.data.model.Project
import org.example.data.model.State
import domain.use_case.authentication.GetCurrentUserUseCase
import domain.use_case.project.AddProjectStateUseCase
import java.util.*

class CreateStateUseCase(
    private val addProjectStateUseCase: AddProjectStateUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) {
    suspend fun createState(name: String, project: Project): State {
            if (name.isBlank()) throw (IllegalArgumentException("Create failed : name is Blank !!"))
            val newState = State(id = UUID.randomUUID(), name = name)
            val currentUserId = getCurrentUserUseCase.getCurrentUser()?.id ?: throw Exception()
            addProjectStateUseCase.addStateToProject(currentUserID = currentUserId, project = project, state = newState)
            return newState
    }
}