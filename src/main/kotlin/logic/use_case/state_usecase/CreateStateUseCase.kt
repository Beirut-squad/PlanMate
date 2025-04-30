package org.example.logic.use_case.state_usecase

import org.example.logic.repositories.state_repository.StateRepository
import org.example.models.State

class CreateStateUseCase(
    private val stateRepository: StateRepository
) {
    fun createState(state: State): Result<State> {
        if (state.name.isBlank()) return Result.failure(IllegalArgumentException("Create failed : name is Blank !!"))
        val state = State(state.id, state.name)
        return stateRepository.createState(state)
    }
}