package org.example.logic.use_cases.state_usecase

import org.example.logic.repositories.state_repository.StateRepository
import org.example.models.State
import java.util.UUID

class EditStateUseCase(
    private val stateRepository: StateRepository
) {
    fun editState(state : State , newName: String, projectId: UUID): Result<State>{
        if (newName.isBlank()) return Result.failure(IllegalArgumentException("Edit failed : name is Blank !!"))
        state.name = newName
        val updateState = State(state.id,projectId,state.name)
        return stateRepository.editState(updateState)
    }
}