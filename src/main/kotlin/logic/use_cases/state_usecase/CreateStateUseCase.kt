package org.example.logic.use_cases.state_usecase

import org.example.logic.repositories.state_repository.StateRepository
import org.example.models.State
import java.util.*

class CreateStateUseCase(
    private val stateRepository: StateRepository
) {
    fun createState(name: String, projectId: UUID): Result<State> {
        if (name.isBlank()) return Result.failure(IllegalArgumentException("Create failed : name is Blank !!"))
        val newState = State(id = UUID.randomUUID(), name = name, projectId = projectId)
        return stateRepository.createState(newState)
    }
}