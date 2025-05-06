package org.example.logic.use_cases.state_usecase

import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.logic.repositories.state_repository.StateRepository
import org.example.logic.use_cases.project_manegment.AddStateToProjectUseCase
import org.example.models.State
import java.util.*

class CreateStateUseCase(
    private val projectRepository: ProjectRepository,
    private val logProjectUseCase: AddStateToProjectUseCase
) {
    fun createState(name: String, projectId: UUID): Result<State> {
        return runCatching {
            if (name.isBlank()) throw (IllegalArgumentException("Create failed : name is Blank !!"))
            val newState = State(id = UUID.randomUUID(), name = name)
            projectRepository.addStateToProject(projectId = projectId, state = newState)
            logProjectUseCase.addStateToProject(projectId, newState)
            newState
        }
    }
}