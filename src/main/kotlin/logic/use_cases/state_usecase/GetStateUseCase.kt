package org.example.logic.use_cases.state_usecase

import org.example.logic.repositories.state_repository.StateRepository
import org.example.models.State
import java.util.UUID

class GetStateUseCase(
    private val stateRepository: StateRepository
) {
    fun getState(projectId: UUID): Result<State>{
        return stateRepository.getState(projectId)
    }
}