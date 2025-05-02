package org.example.logic.use_case.state_usecase

import org.example.logic.repositories.state_repository.StateRepository
import org.example.models.State

class EditStateUseCase(
    private val stateRepository: StateRepository
) {
    fun editState(state : State , newName: String): Result<State>{
        if (newName.isBlank()) return Result.failure(IllegalArgumentException("Edit failed : name is Blank !!"))
        val updateState = State(state.id, newName)
        return stateRepository.editState(updateState)
    }
}