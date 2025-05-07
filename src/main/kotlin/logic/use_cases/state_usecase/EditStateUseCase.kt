package org.example.logic.use_cases.state_usecase

import org.example.logic.repositories.state_repository.StateRepository
import org.example.models.State

class EditStateUseCase(
    private val stateRepository: StateRepository
) {
    fun editState(state : State , newName: String): State{
        if (newName.isBlank()) throw (IllegalArgumentException("Edit failed : name is Blank !!"))
        val updateState = State(state.id, newName)
        return stateRepository.editState(updateState)
    }
}