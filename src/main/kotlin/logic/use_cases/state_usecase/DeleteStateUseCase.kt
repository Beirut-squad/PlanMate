package org.example.logic.use_cases.state_usecase

import org.example.logic.repositories.state_repository.StateRepository
import java.util.UUID

class DeleteStateUseCase(
    private val stateRepository: StateRepository
) {
    fun deleteState(id: UUID): Result<Unit> {
        return stateRepository.deleteState(id)
    }

}